package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import com.zuoer.sofa.blog.base.utils.ArrayUtils;
import com.zuoer.sofa.blog.base.utils.ListUtils;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoer
 * @version UserDefindSqlElementGenerator, v 0.1 2020/3/18 12:11 zuoer Exp $
 */
public class UserDefindSqlElementGenerator extends AbstractXmlElementGenerator {

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb.append("ceshi end and end");
        System.out.println(sb.delete(sb.lastIndexOf("end"), sb.length()));
    }

    @Override
    public void addElements(XmlElement parentElement) {
        //找到表名对应的xml文件，规则为当前运行目录下的 tables/table_name.xml
        try {
            File sqlFile = new File(context.getProperty("targetRunPath") + "/tables", introspectedTable.getTableConfiguration().getTableName() + ".xml");
            if (!sqlFile.exists()) {
                //不存在就算了
                System.out.println("文件不存在，跳过，sqlFile=" + sqlFile.getPath());
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new FileReader(sqlFile)));
            /**
             * 1、最外层的 extraparams 这一块需要直接生成，不是作为sql而是作为引用条件，但是mybatis中没有这个，需要转换成sql节点
             * 2、
             */

            //获取table节点，所有的内容应该都要在这个节点内
            NodeList tableNode = document.getElementsByTagName("table");
            NodeList tableNodeChilds = tableNode.item(0).getChildNodes();

            //遍历子节点，分为两种，一种是extraparams，作为额外参数的引用，一种是operation，具体的sql
            List<Node> extraParamsNodeList = new ArrayList<>();
            List<Node> operationNodeList = new ArrayList<>();
            List<Node> sqlNodeList = new ArrayList<>();
            for (int i = 0; i < tableNodeChilds.getLength(); i++) {
                Node tmp = tableNodeChilds.item(i);
                if (StringUtils.equals(tmp.getNodeName(), "extraparams")) {
                    extraParamsNodeList.add(tmp);
                } else if (StringUtils.equals(tmp.getNodeName(), "operation")) {
                    operationNodeList.add(tmp);
                } else if (StringUtils.equals(tmp.getNodeName(), "sql")) {
                    sqlNodeList.add(tmp);
                }
            }

            //extraparams现在没有进行解析，因为后面的sql是通过CDATA直接整个使用的，等后续进行解析转换了再对extraparams进行解析


            for (Node node : sqlNodeList) {
                XmlElement answer = new XmlElement("sql");
                String id = node.getAttributes().getNamedItem("name").getNodeValue();
//                //为了防止id冲突，在id前面评上表明，其中_换成-，并大写
//                id=StringUtils.replace(StringUtils.toUpperCase(introspectedTable.getTableConfiguration().getTableName()),"_","-")+"-"+id;

                answer.addAttribute(new Attribute(
                        "id", id)); //$NON-NLS-1$
                context.getCommentGenerator().addComment(answer);

                StringBuilder sb = new StringBuilder();
                //此处sql下的内容应为CDATA包含，所以可以直接获取文本方式获取
                sb.append(node.getTextContent());
                answer.addElement(new TextElement(sb.toString()));
                parentElement.addElement(answer);
            }

            for (Node node : operationNodeList) {
                Node sqlNode = null;
                Node extraParamsNode = null;
                Node sqlMapNode = null;

                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node tmp = node.getChildNodes().item(j);
                    if (StringUtils.equals("sql", tmp.getNodeName())) {
                        sqlNode = tmp;
                    } else if (StringUtils.equals("extraparams", tmp.getNodeName())) {
                        extraParamsNode = tmp;
                    } else if (StringUtils.equals("sqlmap", tmp.getNodeName())) {
                        sqlMapNode = tmp;
                    }
                }

                String sqlName = node.getAttributes().getNamedItem("name").getNodeValue();
                XmlElement answer;
                //根据方法名字不同，节点名也不同
                if (sqlName.startsWith("select") || sqlName.startsWith("search")) {
                    answer = new XmlElement("select");
                } else if (sqlName.startsWith("insert")) {
                    answer = new XmlElement("insert");
                } else if (sqlName.startsWith("update")) {
                    answer = new XmlElement("update");
                } else if (sqlName.startsWith("delete")) {
                    answer = new XmlElement("delete");
                } else {
                    throw new RuntimeException("根据sql生成自定义mapper失败,请确认name前缀，目前支持select,search,update,insert，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + sqlName);
                }
                String resultTypeInOperation = node.getAttributes().getNamedItem("resultType") == null ? null : node.getAttributes().getNamedItem("resultType").getNodeValue();
                String paramtype = node.getAttributes().getNamedItem("paramtype") == null ? null : node.getAttributes().getNamedItem("paramtype").getNodeValue();
                answer.addAttribute(new Attribute("id", sqlName));

                String sql="";
                if (sqlMapNode != null) {
                    sql=sqlMapNode.getTextContent();
                } else if (sqlNode != null) {
                    sql = sqlNode.getTextContent();
                }else{
                    throw new RuntimeException("根据sql生成自定义mapper失败,未找到sql语句，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + sqlName);
                }

                //如果是select 则必须确认结果集
                if (sqlName.startsWith("select")) {
                    if (StringUtils.isNotEmpty(resultTypeInOperation)) {
                        answer.addAttribute(new Attribute("resultMap", new FullyQualifiedJavaType(resultTypeInOperation).getFullyQualifiedName()));
                    } else {
                        String[] returnColumns = StringUtils.trim(StringUtils.split(StringUtils.substringBetween(sql, "select", "from"), ","));
                        if (ArrayUtils.getLength(returnColumns) == 1) {
                            answer.addAttribute(new Attribute("resultMap", new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()).getFullyQualifiedName()));
                        } else {
                            answer.addAttribute(new Attribute("resultMap", "BaseResultMap"));
                        }
                    }
                }



                StringBuilder realSql = new StringBuilder();

                //如果sqlmap，按照规则可以直接获取sqlmap中的sql放入节点中
                if (sqlMapNode != null) {
                    realSql.append(sqlMapNode.getTextContent());
                } else if (sqlNode != null) {
                    //如果是sql，要把问号替换成#{columnName}

                    //此处要把 update select insert 分开处理
                    String parameterPart;
                    String sqlAfterParameter;
                    if (sqlName.startsWith("select")) {
                        if(StringUtils.isNotEmpty(paramtype)){
                            answer.addAttribute(new Attribute("parameterType", paramtype));
                        }
                        //按照规则，这里不应该出现search
                        String selectPart = StringUtils.substringBefore(sql, "where");
                        if (StringUtils.contains(sql, "group by")) {
                            //如果有group by 截取groupBy和where之间的就好了
                            parameterPart = StringUtils.substringBetween(sql, "where", "group by");
                            sqlAfterParameter = StringUtils.substringAfter(sql, "group by");
                            //由于group by 被截掉，如果有，则需要拼上去
                            if(StringUtils.isNotEmpty(sqlAfterParameter)){
                                sqlAfterParameter=" group by "+sqlAfterParameter;
                            }
                        } else if(StringUtils.contains(sql, "order by")){
                            //没有就截取order by之间的
                            parameterPart = StringUtils.substringBetween(sql, "where", "order by");
                            sqlAfterParameter = StringUtils.substringAfter(sql, "order by");
                            //由于order by 被截掉，如果有，则需要拼上去
                            if(StringUtils.isNotEmpty(sqlAfterParameter)){
                                sqlAfterParameter=" order by "+sqlAfterParameter;
                            }
                        }else{
                            //都没有，直接去where后面的
                            parameterPart = StringUtils.substringAfter(sql, " where ");
                            sqlAfterParameter = null;
                        }


                        //然后进行拼接
                        realSql.append(selectPart);


                    } else if (sqlName.startsWith("insert")) {
                        parameterPart = null;
                        sqlAfterParameter = null;
                        String insertSatrt = StringUtils.substringBefore(sql, "(");
                        String colmunNamePart = StringUtils.substringBetween(StringUtils.substringBeforeLast(sql, "values"), "(", ")");
                        String valuePart = StringUtils.substringBetween(StringUtils.substringAfterLast(sql, "values"), "(", ")");
                        List<String> columnList = ListUtils.toList(StringUtils.split(colmunNamePart, ","));
                        List<String> valueList = ListUtils.toList(StringUtils.split(valuePart, ","));
                        if (ListUtils.size(columnList) != ListUtils.size(valueList)) {
                            throw new RuntimeException("根据sql生成自定义mapper失败,insert中column数量和value数量不一致，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + sqlName);
                        }
                        realSql.append(insertSatrt).append("(").append(colmunNamePart).append(") values ").append("(");
                        for (int i = 0; i < ListUtils.size(columnList); i++) {
                            String value = valueList.get(i);
                            if (StringUtils.equals(value, "?")) {
                                realSql.append("#{").append(columnList.get(i)).append("}").append(",");
                            } else {
                                realSql.append(value);
                            }
                        }
                        realSql.delete(realSql.lastIndexOf(","), realSql.length());
                        realSql.append(")");
                    } else if (sqlName.startsWith("update")) {
                        String beforeSetPart = StringUtils.substringBefore(sql, "set");
                        String updatePart = StringUtils.substringBetween(sql, "set", "where");
                        parameterPart = StringUtils.substringAfter(sql, "where");
                        sqlAfterParameter = null;
                        //然后进行拼接
                        realSql.append(beforeSetPart);
                        realSql.append(" set ");

                        if (StringUtils.isNotEmpty(updatePart)) {
                            for (String each : StringUtils.split(updatePart, ",")) {
                                if (StringUtils.contains(each, "?")) {
                                    String columnName = StringUtils.trim(each.replace("=", "").replace("?", ""));
                                    realSql.append(columnName).append("=").append("#{").append(columnName).append("}").append(" , ");
                                } else {
                                    realSql.append(each).append(" , ");
                                }
                            }
                        }
                        realSql.delete(realSql.lastIndexOf(","), realSql.length());

                    } else if (sqlName.startsWith("delete")) {
                        sqlAfterParameter=null;
                        parameterPart = StringUtils.substringAfter(sql, "where");
                        String beforeWherePart = StringUtils.substringBefore(sql, "where");
                        realSql.append(beforeWherePart);
                    } else{
                        throw new RuntimeException("根据sql生成自定义mapper失败,请确认name前缀，简单sql目前支持select,update,insert，tableName=" + introspectedTable.getTableConfiguration().getTableName() + ",sqlName=" + sqlName);
                    }
                    if (StringUtils.isNotEmpty(parameterPart)) {
                        realSql.append(" where ");
                        int endIndex = parameterPart.indexOf("?");
                        while (endIndex >= 0) {
                            String columnName = StringUtils.trim(StringUtils.substringBefore(parameterPart.substring(0, endIndex), "="));
                            realSql.append(columnName).append("=").append("#{").append(columnName).append("}").append(" and ");
                            parameterPart = parameterPart.substring(endIndex + 1);
                            endIndex = parameterPart.indexOf("?");
                        }

                        realSql.delete(realSql.lastIndexOf("and"), realSql.length());
                    }
                    if (StringUtils.isNotEmpty(sqlAfterParameter)) {
                        realSql.append(sqlAfterParameter);
                    }


                }
                context.getCommentGenerator().addComment(answer);
                answer.addElement(new TextElement(realSql.toString()));
                parentElement.addElement(answer);


            }


        } catch (Exception e) {
            throw new RuntimeException("根据sql生成自定义mapper失败", e);
        }


    }
}
