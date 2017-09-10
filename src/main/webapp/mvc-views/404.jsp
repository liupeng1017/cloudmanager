<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404</title>
    <jsp:include page="../common/backend_common.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<div class="main-content-inner">
    <div class="page-content">
        <!-- /.ace-settings-container -->
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="error-container">
                    <div class="well">
                        <h1 class="grey lighter smaller">
                            <span class="blue bigger-125">
                                <i class="ace-icon fa fa-sitemap"></i>
                                404
                            </span>
                            页面找不到
                        </h1>
                        <hr>
                        <div>
                            <div class="space"></div>
                            <h4 class="smaller">尝试下面的方法:</h4>

                            <ul class="list-unstyled spaced inline bigger-110 margin-15">
                                <li>
                                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                                    检查输入的页面地址
                                </li>
                                <li>
                                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                                    阅读FAQ
                                </li>
                                <li>
                                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                                    联系管理员
                                </li>
                            </ul>
                        </div>

                        <hr>
                        <div class="space"></div>

                        <div class="center">
                            <a href="javascript:history.back()" class="btn btn-grey">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                上一页
                            </a>

                            <a href="#" class="btn btn-primary">
                                <i class="ace-icon fa fa-tachometer"></i>
                                Dashboard
                            </a>
                        </div>
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>
</body>
</html>
