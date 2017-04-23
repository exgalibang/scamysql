<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<title>Restful风格接口测试</title>
	<base href="<%=basePath%>">
	<!-- jsp文件头和头部 -->
	<%@ include file="../admin/top.jsp"%> 
	</head> 
<body>
		
<div class="container-fluid" id="main-container">

<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


 	<div class="span12">
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
				<h4 class="lighter">Restful风格接口测试</h4>
			</div>
			<div class="widget-body">
			 <div class="widget-main">
					<div class="step-content row-fluid position-relative">
						<label style="float:left;padding-left: 5px;"><span class="lbl">请求方式：</span></label>
						<label style="float:left;padding-left: 5px;"><input name="form-field-radio1" id="form-field-radio1" onclick="setType('GET');" type="radio" value="icon-edit" checked="checked"><span class="lbl">GET</span></label>
						<label style="float:left;padding-left: 15px;"><input name="form-field-radio1" id="form-field-radio2" onclick="setType('POST');" type="radio" value="icon-edit" ><span class="lbl">POST</span></label>
						<label style="float:left;padding-left: 15px;"><input name="form-field-radio1" id="form-field-radio3" onclick="setType('PUT');" type="radio" value="icon-edit" ><span class="lbl">PUT</span></label>
						<label style="float:left;padding-left: 15px;"><input name="form-field-radio1" id="form-field-radio4" onclick="setType('DELETE');" type="radio" value="icon-edit" ><span class="lbl">DELETE</span></label>
						
						<label style="float:left;padding-left: 15px;"><span class="lbl">当前页:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="currentPage_S" id="currentPage_S" style="width:25px;" type="text" value="1" title="查询当前第几页，只能输入数字"></label>
						<label style="float:left;padding-left: 5px;"><span class="lbl">每页数:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="showCount_S" id="showCount_S" style="width:25px;" type="text" value="10" title="查询每页显示的记录数，只能输入数字"></label>
						<label style="float:left;padding-left: 15px;"><span class="lbl">账号:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="username_S" id="username_S" style="width:80px;" type="text" value="" title="接口测试必须先登录"></label>
						<label style="float:left;padding-left: 5px;"><span class="lbl">密码:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="password_S" id="password_S" style="width:80px;" type="password" value="" title="登录账号密码"></label>
					</div>
					<div class="step-content row-fluid position-relative">
						<label style="float:left;padding-left: 5px;"><span class="lbl">Token:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="token_S" id="token_S" style="width:180px;" type="text" value="" title="接口测试token，登录后返回信息中获取"></label>
						<label style="float:left;padding-left: 5px;"><span class="lbl">关键字:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="keyWord_S" id="keyWord_S" style="width:90px;" type="text" value="" title="GET操作查询关键字"></label>
						<label style="float:left;padding-left: 5px;"><span class="lbl">JSON参数:</span></label>
						<label style="float:left;padding-left: 5px;"><input name="S_TYPE_S" id="S_TYPE_S" style="width:300px;" type="text" value="" title="更新操作传的json数据"></label>
					</div>
					<div class="step-content row-fluid position-relative">
						<div style="float: left;">
							<span class="input-icon">
								<input type="text" id="serverUrl" title="输入请求地址" value="<%=basePath%>restful/appU/loginApp" style="width:450px;">
								<i class="icon-globe"></i>
							</span>
						</div>
						<div>
							<!-- 							
							&nbsp;&nbsp;<button onclick="sendSever()">请求</button>
							&nbsp;&nbsp;<button onclick="gReload()">重置</button>
							-->
							&nbsp;<a class="btn btn-small btn-success" onclick="testSever();">内部请求</a>
							<a class="btn btn-small btn-success" onclick="sendSever();">跨域请求</a>
							<a class="btn btn-small btn-info" onclick="gReload();">重置</a> 
						</div>
					</div>
					
					<div class="step-content row-fluid position-relative">
					<textarea id="json-field" title="返回结果" class="autosize-transition span12" style="width:690px;height:200px;"></textarea>
				 	</div>
				 	
				 	<div class="step-content row-fluid position-relative">
					<label style="float:left;padding-left: 35px;">服务器响应：<font color="red" id="stime">-</font>&nbsp;毫秒</label>
					<label style="float:left;padding-left: 35px;">客户端请求：<font color="red" id="ctime">-</font>&nbsp;毫秒</label>
					</div>
					
					<div class="step-content row-fluid position-relative">
						<ul class="unstyled spaced2">
							<li class="text-warning orange"><i class="icon-warning-sign"></i>
								测试接口说明：跨域接口测试请选择跨域请求，否则请选择内部请求，内部接口测试请先登录；
								</br>　　如查询接口：<%=basePath%>restful/appuser/会员账号
								</br>　　修改数据接口为：<%=basePath%>restful/appuser/853
							</li>
							<li class="text-warning green"><i class="icon-star blue"></i>
								请求参数说明：GET请求用来获取数据，POST请求新增数据，PUT请求修改数据，DELETE请求删除数据；
								</br>　　GET请求时不需要填JSON参数,可在关键字处输入要查询的关键词；
								</br>　　POST请求时JSON参数格式：[{"name":"测试用户","username":"ceshi","password":"123456"}]
								</br>　　PUT请求时JSON参数格式：{"name":"测试用户修改","username":"ceshi1","password":"123456"}
							</li>
						</ul>
					</div>
					
				 
				 <input type="hidden" name="S_TYPE" id="S_TYPE" value="GET"/>
				 
			 </div><!--/widget-main-->
			</div><!--/widget-body-->
		</div>
	</div>
 
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<!-- 引入 -->
		<!--MD5-->
		<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<!--引入属于此页面的js -->
		<script type="text/javascript" src="static/js/myjs/interfaceTest.js"></script>
		
	</body>
</html>

