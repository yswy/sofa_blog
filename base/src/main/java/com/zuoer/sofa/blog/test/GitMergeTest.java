package com.zuoer.sofa.blog.test;

import com.zuoer.sofa.blog.base.utils.ArrayUtils;
import com.zuoer.sofa.blog.base.utils.DateUtils;
import com.zuoer.sofa.blog.base.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * git合并代码
 *
 * @author zuoer
 * @version GitMergeTest, v 0.1 2020/4/20 9:58 zuoer Exp $
 */
public class GitMergeTest {

    private static String rootDir = "D:\\合并代码";

    private static FileWriter logFileWriter;

    private static List<String> mergeSuccessAppCode=new ArrayList<>();

    private static List<String> mergeConflictAppCode=new ArrayList<>();

    public static void main(String[] args) throws Exception {
        mergeInner();
    }

    private static void log(String message) throws Exception {
        System.out.println(message);
        IOUtils.write(message + "\r\n", logFileWriter);
        logFileWriter.flush();
    }


    protected static void mergeInner() throws Exception {
        File logFile = new File("d:/git_merge_log/" + DateUtils.getLongDateString(new Date()) + ".txt");
        logFile.getParentFile().mkdirs();
        logFileWriter = new FileWriter(logFile);
        String content = IOUtils.toString(GitMergeTest.class.getResourceAsStream("/git/mergeBranches.txt"));
        List<String> existed = new ArrayList<String>();

        for (String line : StringUtils.splitLine(content)) {
            String[] lineInfo = StringUtils.split(line, "@");

            lineInfo = StringUtils.trim(lineInfo);
            if (ArrayUtils.getLength(lineInfo) < 2) {
                continue;
            }
            if (StringUtils.startsWith(line, "#")) {
                continue;
            }
            if (existed.contains(line)) {
                continue;
            }
            existed.add(line);
            String appCode = lineInfo[0];
            String fromBranchName = lineInfo[1];
            //默认合并到develop
            String toBranchName = ArrayUtils.getLength(lineInfo) > 2 ? lineInfo[2] : "develop";
            log("----------------------------------------------------------");
            log("\r\n\r\n准备处理：" + appCode + "从" + fromBranchName + "合并到" + toBranchName);
            boolean conflict = mergeOne(appCode, fromBranchName, toBranchName);
            if(conflict){
                mergeConflictAppCode.add(appCode);
                log("\r\n\r\n合并有冲突，请手工处理之后提交");
            }else{
                mergeSuccessAppCode.add(appCode);
            }
        }

        System.out.println("----------------------------------合并结果清单----------------------------------");
        System.out.println("以下是合并成功的：");
        mergeSuccessAppCode.forEach(e->System.out.println(e));
        System.out.println("以下是有冲突的，需要手工处理：");
        mergeConflictAppCode.forEach(e->System.out.println(e));
    }

    private static CredentialsProvider createCredentialsProvider() {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("", "");
        return credentialsProvider;
    }

    /**
     * 执行合并
     *
     * @return true表示有冲突，false表示无冲突
     * @throws Exception
     */
    public static boolean mergeOne(String appCode, String fromBranchName, String toBranchName) throws Exception {
        //先确认目录是否占用
        File dir = new File(rootDir + "\\" + appCode);
        if (dir.exists()) {
            FileUtils.deleteDirectory(dir);
            System.out.println("目录已存在，删除原目录.....");
        }

        //先clone代码
        CloneCommand cloneCommand= Git.cloneRepository().setURI("https://git.benchdev.cn/legend/"+appCode+".git");
//        CloneCommand cloneCommand = Git.cloneRepository().setURI("https://git-dev.benchdev.cn/zuoer/" + appCode + ".git");
        cloneCommand.setDirectory(dir);

        cloneCommand.setCredentialsProvider(createCredentialsProvider());
        cloneCommand.call();
        System.out.println("代码clone完成....");

        Git git = Git.open(dir);

//        //罗列所有分支
//        String trueFromName = "";
//        Ref fromRef = null;
//        String trueToName = toBranchName;
//
//        List<Ref> branchList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//        for (Ref ref : branchList) {
//            if (StringUtils.endsWith(ref.getName(), fromBranchName)) {
//                trueFromName = ref.getName();
//                fromRef = ref;
//            }
//        }


        //先切换到来源分支,获取需要合并的来源Ref
        CheckoutCommand checkoutFreomCommand=git.checkout().setName(fromBranchName).setCreateBranch(true).setOrphan(true).setForce(true);
        Ref fromRef=checkoutFreomCommand.call();
        System.out.println("切换到目标分支"+fromBranchName);
        //拉取下最新的
        git.pull().setCredentialsProvider(createCredentialsProvider()).call();

        //切换到目标分支
        CheckoutCommand checkoutToCommand = git.checkout().setName(toBranchName).setCreateBranch(true).setOrphan(true).setForce(true);
        Ref toRef =checkoutToCommand.call();
        System.out.println("切换到目标分支" + toBranchName+toRef);

        //进行合并
        MergeResult mergeResult = git.merge().include(fromRef).call();

        boolean conflict=!MapUtils.isEmpty(mergeResult.getConflicts());

        if(!conflict){
            //若没冲突，直接提交并push
            git.commit().setMessage("Merge remote-tracking branch "+fromBranchName+" into " +toBranchName).call();
            git.push().setCredentialsProvider(createCredentialsProvider()).add(toBranchName).call();
            System.out.println("无冲突，直接提交");

        }
        return conflict;
    }
}
