/**
 *    Copyright 2006-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zuoer.sofa.blog.gen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.util.messages.Messages;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Goal which generates MyBatis/iBATIS artifacts.
 */
@Mojo(name = "generate",  requiresProject = false)
public class MyBatisGeneratorMojo extends AbstractMojo {
    
    private ThreadLocal<ClassLoader> savedClassloader = new ThreadLocal<ClassLoader>();

    /**
     * Maven Project.
     *
     */
    @Parameter(defaultValue = "${targetPath}")
    private String targetRunPath;

    /**
     * Output Directory.
     */
    @Parameter(property = "mybatis.generator.outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/mybatis-generator", required = true)
    private File outputDirectory;

    /**
     * Location of the configuration file.
     */
    private File configurationFile;

    /**
     * Specifies whether the mojo writes progress messages to the log.
     */
    @Parameter(property = "mybatis.generator.verbose", defaultValue = "false")
    private boolean verbose;

    /**
     * Specifies whether the mojo overwrites existing Java files. Default is false.
     * <br>
     * Note that XML files are always merged.
     */
    @Parameter(property = "mybatis.generator.overwrite", defaultValue = "true")
    private boolean overwrite;


    @Override
    public void execute() throws MojoExecutionException {
        // add resource directories to the classpath.  This is required to support
        // use of a properties file in the build.  Typically, the properties file
        // is in the project's source tree, but the plugin classpath does not
        // include the project classpath.
        String configFilePath=targetRunPath+"\\generatorConfig.xml";
        configurationFile=new File(configFilePath);
        if (configurationFile == null) {
            throw new MojoExecutionException(
                    Messages.getString("RuntimeError.0")); //$NON-NLS-1$
        }

        List<String> warnings = new ArrayList<String>();

        getLog().info("outputDirectory:"+this.outputDirectory.getPath());

        if (!configurationFile.exists()) {
            throw new MojoExecutionException(Messages.getString(
                    "RuntimeError.1", configurationFile.toString())); //$NON-NLS-1$
        }

        //拉取所有表名
        Set<String> fullyqualifiedTables = new HashSet<String>();
        for (File file : FileUtils.listFiles(new File(targetRunPath + "\\tables\\"), new String[]{"xml"}, false)) {
            fullyqualifiedTables.add(FilenameUtils.removeExtension(file.getName()));
        }

        if(fullyqualifiedTables.size()==0){
            getLog().info("there is not tables");
            return;
        }

        //TODO 还不知道是啥东西
        Set<String> contextsToRun = new HashSet<String>();

        try {
            Properties properties=new Properties();
            properties.put("targetRunPath",targetRunPath);

            ConfigurationParser cp = new ConfigurationParser(
                    properties, warnings);
            Configuration config = cp.parseConfiguration(configurationFile);

            ShellCallback callback = new MavenShellCallback(this, overwrite);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                    callback, warnings);
            myBatisGenerator.generate(new MavenProgressCallback(getLog(),
                    verbose), contextsToRun, fullyqualifiedTables);

        } catch (XMLParserException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (SQLException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
        }

        for (String error : warnings) {
            getLog().warn(error);
        }

        restoreClassLoader();
    }


    public File getOutputDirectory() {
        return outputDirectory;
    }
    
    private void saveClassLoader() {
        savedClassloader.set(Thread.currentThread().getContextClassLoader());
    }

    private void restoreClassLoader() {
        Thread.currentThread().setContextClassLoader(savedClassloader.get());
    }
}
