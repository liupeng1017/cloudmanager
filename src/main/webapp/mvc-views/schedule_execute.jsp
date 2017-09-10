<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../common/backend_common.jsp"/>
    <jsp:include page="../common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        定时任务管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            定时任务执行情况查看
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-12">
        <div class="col-xs-12">
            <div class="table-header">
                [${scheduleId}]&nbsp;任务执行情况&nbsp;
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                执行开始时间
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                执行结束时间
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                执行时长(ms)
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                执行结果
                            </th>
                        </tr>
                        </thead>
                        <tbody id="executeResultList"></tbody>
                    </table>
                    <div class="row" id="executeResultPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="executeResultListTemplate" type="x-tmpl-mustache">
{{#executeResultList}}
<tr role="row" class="schedule odd""><!--even -->
    <td>{{start}}</td>
    <td>{{end}}</td>
    <td>{{costMillSeconds}}</td>
    <td>{{showStatus}}</td>
</tr>
{{/executeResultList}}

</script>

<script type="text/javascript">
    $(function () {
        var executeResultListTemplate = $('#executeResultListTemplate').html();
        Mustache.parse(executeResultListTemplate);
        var scheduleId = '${scheduleId}';
        var url = "/admin/schedule/results.json?scheduleId=" + scheduleId;

        loadExecuteResultList();

        function loadExecuteResultList() {
            var pageNo = $("#executeResultPage .pageNo").val() || 1;
            $.ajax({
                url: url,
                data: {
                    pageNo: pageNo,
                    pageSize: $("#pageSize").val()
                },
                type: 'POST',
                success: function (result) {
                    renderPageList(result, url);
                }
            });
        }

        function renderPageList(result, url) {
            if (result.ret) {
                var rendered = Mustache.render(executeResultListTemplate, {
                    "executeResultList": result.data.data,
                    "showStatus": function () {
                        return this.status == 1 ? "已经完成" : (this.status == 0 ? "正在运行" : "出现异常结束");
                    }
                });
                $('#executeResultList').html(rendered);
                var pageSize = $("#pageSize").val();
                var pageNo = $("#executeResultPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "executeResultPage", renderPageList);
            } else {
                showMessage("获取任务执行结果列表", result.msg, false);
            }
        }
    });
</script>
</body>
</html>
