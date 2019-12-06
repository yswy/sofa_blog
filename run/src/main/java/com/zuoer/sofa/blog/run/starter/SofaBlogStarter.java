package com.zuoer.sofa.blog.run.starter;

import com.zuoer.sofa.blog.base.AbstractBenchStarter;
import com.zuoer.sofa.blog.base.application.SofaBlogWebApplication;

/**
 * 
 * @author zuoer
 *
 * @version $Id: NetflixBenchApplication.java, v 0.1 2019年11月11日 上午11:41:23 zuoer Exp $
 */
public class SofaBlogStarter extends AbstractBenchStarter{

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
	    try {
	    	SofaBlogWebApplication app = new SofaBlogWebApplication(SofaBlogStarter.class);
			app.run(args);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}
	
}
