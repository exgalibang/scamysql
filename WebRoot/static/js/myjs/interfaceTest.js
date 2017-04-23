var locat = (window.location+'').split('/'); 
$(function(){
	if('tool'== locat[3]){
		locat =  locat[0]+'//'+locat[2];
	}else{
		locat =  locat[0]+'//'+locat[2]+'/'+locat[3];
	};
});


$(top.hangge());

//重置
function gReload(){
	top.jzts();
	$("#serverUrl").val('');
	$("#json-field").val('');
	$("#S_TYPE_S").val('');
	self.location.reload();
}

//请求类型
function setType(value){
	$("#S_TYPE").val(value);
}
//跨域请求
function sendSever(){
	var locatwww = window.location.host;
	if($("#serverUrl").val()==""){
		$("#serverUrl").tips({
			side:3,
            msg:'输入请求地址',
            bg:'#AE81FF',
            time:2
        });
		$("#serverUrl").focus();
		return false;
	}
	var tUrl = $("#serverUrl").val();
	try {
		var reqWWW = tUrl.split(/\//)[2];
		if(locatwww == reqWWW) {
			$("#serverUrl").tips({
				side:2,
	            msg:'不是跨域请求，请选择ajax请求',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}
	} catch (e) {
		$("#serverUrl").tips({
			side:3,
            msg:'请求地址或参数格式不正确！',
            bg:'#AE81FF',
            time:2
        });
		$("#serverUrl").focus();
		return false;
	}

	var requestMethod = $("#S_TYPE").val();
	var S_TYPE_S = $("#S_TYPE_S").val();	//更新参数json
	//加密方式  (取其中一个参数名+当前日期[格式 20150405]+混淆码",km," 然后md5加密 的值作为 参数FKEY的值提交)
	var paraname = $("#keyWord_S").val();	//查询参数关键字
	var token_S = $("#token_S").val();
	var showCount_S = $("#showCount_S").val();	//查询参数当前页
	var currentPage_S = $("#currentPage_S").val();	//查询参数每页数
	var nowtime = date2str(new Date(),"yyyyMMdd");
	var startTime = new Date().getTime(); //请求开始时间  毫秒
	//验证请求数据格式
	if("POST" == requestMethod) {
		if("" == S_TYPE_S) {
			$("#S_TYPE_S").tips({
				side:3,
				msg:'新增数据时，JSON参数不能为空！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		} else if(S_TYPE_S.indexOf("[") != 0 || S_TYPE_S.lastIndexOf("]") != S_TYPE_S.length-1){
			$("#S_TYPE_S").tips({
				side:3,
				msg:'JSON参数格式不对，请参考请求参数说明！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		}
	} else if("PUT" == requestMethod) {
		var lastIndex = tUrl.lastIndexOf("/");
		if(lastIndex < 0 || !new RegExp(/^[1-9]+[0-9]*]*$/).test(tUrl.substring(lastIndex+1))){
			$("#serverUrl").tips({
				side:3,
	            msg:'修改数据时，请求地址接口名称必须为修改数据ID（正整数结尾）！',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}
		if("" == S_TYPE_S) {
			$("#S_TYPE_S").tips({
				side:3,
				msg:'修改数据时，JSON参数不能为空！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		} else if(!S_TYPE_S.match("^\{(.+:.+,*){1,}\}$")){
			$("#S_TYPE_S").tips({
				side:3,
				msg:'JSON参数格式不对，请参考请求参数说明！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		}
	} else if("DELETE" == requestMethod) {
		var lastIndex = tUrl.lastIndexOf("/");
		if(lastIndex < 0 || !new RegExp(/^[1-9]+[0-9]*]*$/).test(tUrl.substring(lastIndex+1))) {
			$("#serverUrl").tips({
				side:3,
	            msg:'删除数据时，请求地址接口名称必须为删除数据ID（正整数结尾）！',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}
	} 
	
//	if(tUrl.lastIndexOf(".do") < 0) 
//		tUrl = tUrl + ".do";
	
	if(tUrl.indexOf("?") > 0 ){
		tUrl = tUrl + "&apikey=" + token_S + "&tm=" + new Date().getTime();
	} else {
		tUrl = tUrl + "?apikey=" + token_S + "&tm=" + new Date().getTime();
	}
	var post_data={serverUrl:tUrl,requestMethod:requestMethod,apikey:token_S,keyWords:paraname,showCount:showCount_S,currentPage:currentPage_S,jsonData:S_TYPE_S,tm:new Date().getTime()}; 
	top.jzts(); 
	$.ajax({
		type: "POST",
		url: locat+'/tool/apiTest.do',
		//url: locat+'/tool/severTest.do',
    	data: post_data,
		dataType:'json',
		cache: false,
		success: function(data){
			 $(top.hangge());
			 if("success" == data.errInfo){
				 $("#serverUrl").tips({
						side:1,
			            msg:'服务器请求成功',
			            bg:'#75C117',
			            time:5
			     });
				 var endTime = new Date().getTime();  //请求结束时间  毫秒 
				 $("#ctime").text(endTime-startTime);
				 
			 }else{
				 $("#serverUrl").tips({
						side:3,
			            msg:'请求失败,检查URL正误',
			            bg:'#FF5080',
			            time:5
			     });
				 return;
			 }
			 
			 $("#json-field").val(data.result);
			 $("#json-field").tips({
					side:2,
		            msg:'返回结果',
		            bg:'#75C117',
		            time:10
		     });
			 $("#stime").text(data.rTime);
			 
		}
	});
}
//ajax请求
function testSever(){
	var locatwww = window.location.host;
	if($("#serverUrl").val()==""){
		$("#serverUrl").tips({
			side:3,
            msg:'输入请求地址',
            bg:'#AE81FF',
            time:2
        });
		$("#serverUrl").focus();
		return false;
	}
	if($("#username_S").val()==""){
		$("#username_S").tips({
			side:3,
            msg:'请先输入账号密码进行登录',
            bg:'#AE81FF',
            time:2
        });
		$("#username_S").focus();
		return false;
	}
	var tUrl = $("#serverUrl").val();
	try {
		var reqWWW = tUrl.split(/\//)[2];
		if(locatwww != reqWWW) {
			$("#serverUrl").tips({
				side:2,
	            msg:'跨域请求，请选择内部跳转请求',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}
	} catch (e) {
		$("#serverUrl").tips({
			side:3,
            msg:'请求地址或参数格式不正确！',
            bg:'#AE81FF',
            time:2
        });
		$("#serverUrl").focus();
		return false;
	}
	//加密方式  (取其中一个参数名+当前日期[格式 20150405]+混淆码",km," 然后md5加密 的值作为 参数FKEY的值提交)
	var paraname = $("#keyWord_S").val();	//查询参数关键字
	var username = $("#username_S").val();
	var password = $("#password_S").val();
	var token_S = $("#token_S").val();
	var showCount_S = $("#showCount_S").val();	//查询参数当前页
	var currentPage_S = $("#currentPage_S").val();	//查询参数每页数
	var S_TYPE_S = $("#S_TYPE_S").val();	//更新参数json
	var nowtime = date2str(new Date(),"yyyyMMdd");
	var requestMethod = $("#S_TYPE").val();
	var startTime = new Date().getTime(); //请求开始时间  毫秒
	
	//验证请求数据格式
	if("POST" == requestMethod) {
		if("" == S_TYPE_S) {
			$("#S_TYPE_S").tips({
				side:3,
				msg:'新增数据时，JSON参数不能为空！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		} else if(S_TYPE_S.indexOf("[") != 0 || S_TYPE_S.lastIndexOf("]") != S_TYPE_S.length-1){
			$("#S_TYPE_S").tips({
				side:3,
				msg:'JSON参数格式不对，请参考请求参数说明！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		}
	} else if("PUT" == requestMethod) {
		/*var lastIndex = tUrl.lastIndexOf("/");
		if(lastIndex < 0 || !new RegExp(/^[1-9]+[0-9]*]*$/).test(tUrl.substring(lastIndex+1))){
			$("#serverUrl").tips({
				side:3,
	            msg:'修改数据时，请求地址接口名称必须为修改数据ID（正整数结尾）！',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}*/
		if("" == S_TYPE_S) {
			$("#S_TYPE_S").tips({
				side:3,
				msg:'修改数据时，JSON参数不能为空！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		} else if(!S_TYPE_S.match("^\{(.+:.+,*){1,}\}$")){
			$("#S_TYPE_S").tips({
				side:3,
				msg:'JSON参数格式不对，请参考请求参数说明！',
				bg:'#AE81FF',
				time:2
			});
			$("#S_TYPE_S").focus();
			return false;
		}
	} else if("DELETE" == requestMethod) {
		/*var lastIndex = tUrl.lastIndexOf("/");
		if(lastIndex < 0 || !new RegExp(/^[1-9]+[0-9]*]*$/).test(tUrl.substring(lastIndex+1))) {
			$("#serverUrl").tips({
				side:3,
	            msg:'删除数据时，请求地址接口名称必须为删除数据ID（正整数结尾）！',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#serverUrl").focus();
			return false;
		}*/
	} 
	/*if("" != S_TYPE_S && "GET" == requestMethod) {
		$("#form-field-radio2").tips({
			side:3,
            msg:'查询数据时请选择GET请求方式！',
            bg:'#AE81FF',
            time:2
        });
		$("#form-field-radio2").focus();
		return false;
	}*/
	
	if(tUrl.indexOf("?") > 0 ){
		tUrl = tUrl + "&token=" + token_S + "&tm=" + new Date().getTime();
	} else {
		tUrl = tUrl + "?token=" + token_S + "&tm=" + new Date().getTime();
	}
	var post_data={username:username,password:password,token:token_S,keyWords:paraname,showCount:showCount_S,currentPage:currentPage_S}; 
	if("GET" != requestMethod) {
		post_data = S_TYPE_S;
	} 
	top.jzts(); 
	$.ajax({
		type: requestMethod,
		url: tUrl,
		data:post_data, 
		dataType:'json',
		contentType: "application/json; charset=utf-8",   
		//async: false,     // 布尔类型，默认为true 表示请求是否为异步，如果为false表示为同步。
		cache: false,
		success: function(data){
			 $(top.hangge());
			 if("0" != data.reslut_code){
				 $("#serverUrl").tips({
						side:1,
			            msg:'服务器请求成功',
			            bg:'#75C117',
			            time:3
			     });
				 var endTime = new Date().getTime();  //请求结束时间  毫秒 
				 $("#stime").text(endTime-startTime);
				 
			 }else{
				 $("#serverUrl").tips({
						side:3,
			            msg:'请求失败,检查URL正误',
			            bg:'#FF5080',
			            time:3
			     });
				 return;
			 }
			 if(null != data.data && "" != data.data && "undefined" != data.data)
				 $("#token_S").val(data.data.token);
			 var jsons = JSON.stringify(data);
			 $("#json-field").val(jsons);
			 $("#json-field").tips({
					side:2,
		            msg:'返回结果',
		            bg:'#75C117',
		            time:10
		     });
			 var endTime2 = new Date().getTime();  //请求结束时间  毫秒 
			 $("#ctime").text(endTime2-startTime);
		}
	});
}

function intfBox(){
	var intfB = document.getElementById("json-field");
	var intfBt = document.documentElement.clientHeight;
	intfB .style.height = (intfBt  - 320) + 'px';
}
intfBox();
window.onresize=function(){  
	intfBox();
};

//js  日期格式
function date2str(x,y) {
     var z ={y:x.getFullYear(),M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
     return y.replace(/(y+|M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-(v.length>2?v.length:2))});
 	};