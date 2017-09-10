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
        配置管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            管理系统全局配置
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-12">
        <div class="col-xs-12">
            <button class="btn btn-info loadAllMachine" type="button">
                所有机器重新加载配置
            </button>
            <br />
            <br />
            <div class="table-header">
                配置列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 config-add"></i>
                </a>
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
                                配置项
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                配置值
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                配置说明
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                最后操作人
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="configList"></tbody>
                    </table>
                    <div class="row" id="configPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="configListTemplate" type="x-tmpl-mustache">
{{#configList}}
<tr role="row" class="config odd" data-id="{{k}}"><!--even -->
    <td><a class="config-edit" data-id={{k}} style="cursor:pointer;">{{k}}</a></td>
    <td>{{v}}</td>
    <td>{{comment}}</td>
    <td>{{operator}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green config-edit" href="#" data-id="{{k}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/configList}}
</script>

<div id="dialog-config-form" style="display: none;">
    <form id="configForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="configK">配置项</label></td>
                <td>
                    <input type="text" name="k" id="configK" value="" class="text ui-widget-content ui-corner-all">
                </td>
            </tr>
            <tr>
                <td style="width: 80px;"><label for="configV">配置值</label></td>
                <td>
                    <textarea name="v" id="configV" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea>
                </td>
            </tr>
            <tr>
                <td><label for="configComment">备注</label></td>
                <td><textarea name="comment" id="configComment" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        var configListTemplate = $('#configListTemplate').html();
        Mustache.parse(configListTemplate);
        var configMap = {};

        loadConfigList();

        function loadConfigList() {
            var pageSize = $("#pageSize").val();
            var pageNo = $("#configPage .pageNo").val() || 1;
            var url = "/config/page.json";
            $.ajax({
                url: url,
                data: {
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'POST',
                success: function (result) {
                    renderConfigListAndPage(result, url);
                }
            });
        }

        function renderConfigListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0) {
                    var rendered = Mustache.render(configListTemplate, {"configList": result.data.data});
                    $('#configList').html(rendered);
                    $.each(result.data.data, function (i, config) {
                        configMap[config.k] = config;
                    });
                } else {
                    $('#configList').html('');
                }
                bindConfigClick();
                var pageSize = $("#pageSize").val();
                var pageNo = $("#configPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "configPage", renderConfigListAndPage);
            } else {
                showMessage("获取配置列表", result.msg, false);
            }
        }

        $(".config-add").click(function () {
            $("#dialog-config-form").dialog({
                modal: true,
                title: "新增配置",
                open: function (event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                    $("#configForm")[0].reset();
                    $("#configK").removeAttr("readonly");
                },
                buttons: {
                    "添加": function (e) {
                        e.preventDefault();
                        updateConfig(function (data) {
                            $("#dialog-config-form").dialog("close");
                        }, function (data) {
                            showMessage("新增配置", data.msg, false);
                        });
                    },
                    "取消": function () {
                        $("#dialog-config-form").dialog("close");
                        loadConfigList();
                    }
                }
            });
        });

        $(".loadAllMachine").click(function () {
            $.ajax({
                url: "/config/reloadAll.json",
                data: {},
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        $(result.data).each(function(i, str) {
                            if(str.indexOf("success") != -1) {
                                showMessage("机器加载配置", str, true);
                            }else {
                                showMessage("机器加载配置", str, false);
                            }
                        });
                    } else {
                        showMessage("所有机器加载配置", "服务器处理错误,请稍候重试", false);
                    }
                },
                error: function() {
                    showMessage("所有机器加载配置出错", "服务器处理错误,请稍候重试", false);
                }
            })
        });

        function updateConfig(successCallback, failCallback) {
            $.ajax({
                url: "/config/save.json",
                data: $("#configForm").serializeArray(),
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
                },
                error: function() {
                    showMessage("更新配置出错", "服务器处理错误,请稍候重试", false);
                }
            });
        }

        function bindConfigClick() {
            $(".config-edit").click(function (e) {
                e.preventDefault();
                var configK = $(this).attr("data-id"); // 选中的部门id
                $("#dialog-config-form").dialog({
                    modal: true,
                    title: "编辑配置",
                    open: function (event, ui) {
                        $("#configForm")[0].reset();
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                        var targetConfig = configMap[configK];
                        if (targetConfig) {
                            $("#configK").val(targetConfig.k);
                            $("#configK").attr("readonly","readonly");
                            $("#configV").val(targetConfig.v);
                            $("#configComment").val(targetConfig.comment);
                        }
                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault();
                            updateConfig(function (data) { // success callback
                                configMap[configK].v = $("#configV").val();
                                configMap[configK].comment = $("#configComment").val();
                                showMessage("更新配置", "需要所有机器重新加载配置,才能使最新的配置生效", true);
                                $("#dialog-config-form").dialog("close");
                                loadConfigList();
                            }, function (data) { // fail callback
                                showMessage("更新配置", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-config-form").dialog("close");
                            loadConfigList();
                        }
                    }
                });
            });
        }
    });
</script>
</body>
</html>
