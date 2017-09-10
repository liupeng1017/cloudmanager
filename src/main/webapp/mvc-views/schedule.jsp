<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../common/backend_common.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        定时任务管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            定时任务调度管理及运行情况查看
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-12">
        <div class="col-xs-12">
            <div class="table-header">
                任务列表&nbsp;&nbsp;
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                任务名称
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                表达式
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                上次执行时间
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                执行时长(ms)
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                下次执行时间
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="scheduleList"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="dialog-schedule-form" style="display: none;">
    <form id="scheduleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="settingId">任务</label></td>
                <td>
                    <span class="settingId"></span>
                    <input type="hidden" name="id" id="settingId"/>
                </td>
            </tr>
            <tr>
                <td style="width: 80px;"><label for="settingCron">调度表达式</label></td>
                <td>
                    <input type="text" name="cron" id="settingCron" value="" class="text ui-widget-content ui-corner-all">
                    <span class="cron"></span>
                </td>
            </tr>
            <tr>
                <td style="width: 80px;"><label for="settingStatus">状态</label></td>
                <td>
                    <select id="settingStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="-1" disabled>从未开始</option>
                        <option value="1">运行</option>
                        <option value="0">暂停</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<script id="scheduleListTemplate" type="x-tmpl-mustache">
{{#scheduleList}}
<tr role="row" class="schedule odd""><!--even -->
    <td><a class="schedule-show" data-id="{{groupId}}_{{scheduleId}}" style="cursor:pointer;" >{{groupId}}_{{scheduleId}}</a></td>
    <td>{{cron}}</td>
    <td>{{lastExecuteTime}}</td>
    <td>{{costMillSeconds}}</td>
    <td>{{nextExecuteTime}}</td>
    <td>{{showStatus}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green schedule-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/scheduleList}}
</script>

<script type="text/javascript">
    $(function () {
        var scheduleListTemplate = $('#scheduleListTemplate').html();
        Mustache.parse(scheduleListTemplate);
        var settingMap = {};

        loadScheduleList();

        function loadScheduleList() {
            $.ajax({
                url: "/admin/schedule/all.json",
                data: {},
                type: 'POST',
                success: function (result) {
                    renderScheduleList(result);
                },
                error: function() {
                    showMessage("加载任务列表", "服务器处理错误,请稍候重试", false);
                }
            });
        }

        function renderScheduleList(result) {
            if (result.ret) {
                var rendered = Mustache.render(scheduleListTemplate, {
                    "scheduleList": result.data,
                    "showStatus": function () {
                        return this.status == 1 ? "运行中" : (this.status == 0 ? "暂停" : "未开始过");
                    }
                });
                $('#scheduleList').html(rendered);
                $.each(result.data, function (i, setting) {
                    settingMap[setting.id] = setting;
                });
                bindClick();
            } else {
                showMessage("获取任务列表", result.msg, false);
            }
        }

        function bindCronBlur() {
            $("#settingCron").mouseup(function (e) {
                e.preventDefault();
                calCronAndShow();
            });
        }

        function calCronAndShow() {
            var cron = $("#settingCron").val();
            if (cron == '') {
                $(".cron").html('');
                return;
            }
            $.ajax({
                url: "/admin/schedule/cron.json",
                data: {
                    cron: cron
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        var cronStr = "";
                        $.each(result.data, function (i, str) {
                            cronStr += "<div>" + str + "</div>";
                        });
                        $(".cron").html(cronStr);
                    } else {
                        showMessage("计算新的调度时间出错", result.msg, false);
                        $(".cron").html('');
                    }
                },
                error: function() {
                    showMessage("计算调度时间出错", "服务器处理错误,请稍候重试", false);
                    $(".cron").html('');
                }
            });
        }

        function bindClick() {
            $(".schedule-show").click(function (e) {
                e.preventDefault();
                var scheduleId = $(this).attr("data-id");
                window.location.href = "/admin/schedule/executePage.do?scheduleId=" + scheduleId;
            });

            $(".schedule-edit").click(function (e) {
                e.preventDefault();
                var id = $(this).attr("data-id");
                $("#dialog-schedule-form").dialog({
                    modal: true,
                    title: "任务配置",
                    open: function (event, ui) {
                        $("#scheduleForm")[0].reset();
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                        var targetSetting = settingMap[id];
                        $("#settingCron").val(targetSetting.cron);
                        $("#settingStatus").val(targetSetting.status);
                        $("#settingId").val(id);
                        $(".settingId").html(targetSetting.groupId + "_" + targetSetting.scheduleId);
                        bindCronBlur();
                        calCronAndShow();
                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault();
                            updateSetting(function (data) { // success callback
                                settingMap[id].cron = $("#settingCron").val();
                                settingMap[id].status = $("#settingStatus").val();
                                $("#dialog-schedule-form").dialog("close");
                                showMessage("更新调度配置成功", "系统将根据最新调度配置处理定时任务", true);
                                loadScheduleList();
                            }, function (data) { // fail callback
                                showMessage("更新配置出错", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-schedule-form").dialog("close");
                            loadScheduleList();
                        }
                    }
                });
            });
        }

        function updateSetting(successCallback, failCallback) {
            $.ajax({
                url: "/admin/schedule/update.json",
                data: $("#scheduleForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        if (successCallback) {
                            successCallback(result); // 使用callback.call() 这种无法传递参数
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
                ,
                error: function() {
                    if (failCallback) {
                        failCallback(result);
                    }
                }
            });
        }
    });
</script>
</body>
</html>
