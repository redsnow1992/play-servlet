# -*- coding: utf-8 -*-
# 将play项目打成war
# play项目的地址，项目名称，所需play-servlet.jar地址

require "fileutils"

def play2war(project_name, project_path, servlet_jar_path, war_dir)
  FileUtils.cd(project_path, verbose: true)
  cmd = "sbt dist"
  puts cmd
  system(cmd)
  FileUtils.cd(File.join(project_path, "target/universal"), verbose: true)
  file = Dir.glob("#{project_name}*.zip").first
  dir_name = File.basename(file, ".zip")
  FileUtils.rm_rf(dir_name, verbose: true) if Dir.exists?(dir_name)
  system("unzip #{file}")
  puts "unzip #{file} done ..."
  project_war_dir = build_war_dir(war_dir, project_name, "dev")
  web_inf_dir = File.join(project_war_dir, "WEB-INF")
  FileUtils.cd(dir_name, verbose: true)

  # copy lib和servlet jar
  FileUtils.cp_r("lib", web_inf_dir, verbose: true)
  FileUtils.cp(servlet_jar_path, File.join(web_inf_dir, "lib"), verbose: true)

  # 将conf打入项目jar
  project_jar = Dir.glob("#{File.join(web_inf_dir, "lib", project_name)}*sans*.jar").first
  FileUtils.cd("conf", verbose: true)
  system("jar uvf #{project_jar} .")
  # 创建war
  war_name = "#{project_name}.war"
  FileUtils.cd(project_war_dir, verbose: true)
  system("jar cvf #{war_name} .")
  puts "all done."
end

def build_war_dir(war_dir, project_name, mode = "prod")
  project_dir = File.join(war_dir, project_name)
  FileUtils.rm_rf(project_dir, verbose: true)   if Dir.exists?(project_dir)
  FileUtils.mkdir_p(project_dir, verbose: true)
  
  FileUtils.mkdir_p(File.join(project_dir, "WEB-INF"), verbose: true)
  FileUtils.mkdir_p(File.join(project_dir, "META-INF"), verbose: true)
  File.open(File.join(project_dir, "META-INF", "MANIFEST.MF"), "w") do |file|
    file.puts "Manifest-Version: 1.0"
  end
  FileUtils.touch(File.join(project_dir, "META-INF", "war-tracker"), verbose: true)
  unless mode == "prod"
    File.open(File.join(project_dir, "WEB-INF", "web.xml"), "w") do |file|
      file.puts %Q[<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" "http://java.sun.com/dtd/web-app_2_3.dtd">  
<web-app>  
  <context-param>
    <param-name>play.mode</param-name>
    <param-value>#{mode.downcase.capitalize}</param-value>
  </context-param>
</web-app>]
    end
  end

  system("tree #{project_dir}")
  project_dir
end

home = Dir.home
play2war("chezai-scala", "#{home}/Work/chezai-scala/", "#{home}/git_repo/play-servlet/target/scala-2.11/play-servlet_2.11-1.0-SNAPSHOT.jar", "#{home}/war")
