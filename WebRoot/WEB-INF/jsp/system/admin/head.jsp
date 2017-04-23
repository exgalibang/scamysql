<div class="navbar navbar-inverse">
		  <div class="navbar-inner">
		   <div class="container-fluid">
			  <a class="brand"  target="_blank" ><small><i class="icon-leaf"></i> ${pd.SYSNAME}</small> </a> 
			  
			  <ul class="nav ace-nav pull-right">
					<li class="grey">
						<a href="javascript:alert('预留功能,待开发');" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-tasks"></i>
							<span class="badge">1</span>
						</a>
						<ul class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-closer">
							<li class="nav-header">
								<i class="icon-ok"></i> 4 任务完成
							</li>
							
							<li>
								<a href="javascript:alert('预留功能,待开发');">
									<div class="clearfix">
										<span class="pull-left">软件更新</span>
										<span class="pull-right">65%</span>
									</div>
									<div class="progress progress-mini"><div class="bar" style="width:65%"></div></div>
								</a>
							</li>
							
							<li>
								<a href="javascript:alert('预留功能,待开发');">
									<div class="clearfix">
										<span class="pull-left">软件更新</span>
										<span class="pull-right">35%</span>
									</div>
									<div class="progress progress-mini progress-danger"><div class="bar" style="width:35%"></div></div>
								</a>
							</li>
							
							<li>
								<a href="javascript:alert('预留功能,待开发');">
									<div class="clearfix">
										<span class="pull-left">运行测试</span>
										<span class="pull-right">15%</span>
									</div>
									<div class="progress progress-mini progress-warning"><div class="bar" style="width:15%"></div></div>
								</a>
							</li>
							
							<li>
								<a href="javascript:alert('预留功能,待开发');">
									<div class="clearfix">
										<span class="pull-left">Bug 修复</span>
										<span class="pull-right">90%</span>
									</div>
									<div class="progress progress-mini progress-success progress-striped active"><div class="bar" style="width:90%"></div></div>
								</a>
							</li>
							

						</ul>
					</li>
					
					
					
					
					
					
					<!-- <li class="purple" onclick="creatw();">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-bell-alt icon-animated-bell icon-only"></i>
						</a>
					</li>
					 -->
					
					
					<li class="light-blue user-profile">
						<a class="user-menu dropdown-toggle" href="javascript:;" data-toggle="dropdown">
							<img alt="FH" src="static/avatars/user.jpg" class="nav-user-photo" />
							<span id="user_info">
							</span>
							<i class="icon-caret-down"></i>
						</a>
						<ul id="user_menu" class="pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
							<li><a onclick="editUserH();" style="cursor:pointer;"><i class="icon-user"></i> 修改资料</a></li>
							<li id="systemset"><a onclick="editSys();" style="cursor:pointer;"><i class="icon-cog"></i> 系统设置</a></li>
							<!-- <li id="productCode"><a onclick="productCode();" style="cursor:pointer;"><i class="icon-cogs"></i> 代码生成</a></li>
							 -->
							<li class="divider"></li>
							<li><a href="logout"><i class="icon-off"></i> 退出</a></li>
						</ul>
					</li>
			  </ul><!--/.ace-nav-->
		   </div><!--/.container-fluid-->
		  </div><!--/.navbar-inner-->
		</div><!--/.navbar-->
	
	
		<!--引入属于此页面的js -->
		<script type="text/javascript" src="static/js/myjs/head.js"></script>
