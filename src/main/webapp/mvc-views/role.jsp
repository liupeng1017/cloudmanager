<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色管理</title>
    <jsp:include page="../common/backend_common.jsp"/>
    <jsp:include page="../common/aclTree.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
        角色管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护角色与用户, 角色与权限关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            角色列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 role-add"></i>
            </a>
        </div>
        <div id="roleList"></div>
    </div>
    <div class="col-sm-9">
        <div class="tabbable" id="roleTab">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a data-toggle="tab" href="#roleAclTab">
                        角色与权限
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#roleUserTab">
                        角色与用户
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="roleAclTab" class="tab-pane fade in active">
                    <form id="roleAclTree">
                        <p class="well">请先在左侧选择角色</p>
                    </form>
                    <button class="btn btn-info saveRoleAcl" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>

                <div id="roleUserTab" class="tab-pane fade">
                    <p class="well">请先在左侧选择角色</p>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-role-form" style="display: none;">
    <form id="roleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td><label for="roleName">名称</label></td>
                <td>
                    <input type="text" name="name" id="roleName" value="" class="text ui-widget-content ui-corner-all">
                    <input type="hidden" name="id" id="roleId"/>
                </td>
            </tr>
                <td><label for="roleRemark">备注</label></td>
                <td><textarea name="remark" id="roleRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<script id="roleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#roleList}}
        <li class="dd-item dd2-item role-name" id="role_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green role-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red role-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/roleList}}
</ol>
</script>

<script type="text/javascript">
    $(function () {
        var roleMap = {};
        var lastRoleId = -1;
        var selectFirstTab = true;
        // 预编译需要使用的模板
        var roleListTemplate = $('#roleListTemplate').html();
        Mustache.parse(roleListTemplate);

        loadRoleList();

        // 处理点击[新增角色]按钮
        $(".role-add").click(function () {
            $("#dialog-role-form").dialog({
                modal: true,
                title: "新增角色",
                open: function (event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                    $("#roleForm")[0].reset();
                },
                buttons: {
                    "添加": function (e) {
                        e.preventDefault();
                        updateRole(true, function (data) {
                            $("#dialog-role-form").dialog("close");
                        }, function (data) {
                            showMessage("新增角色", data.msg, false);
                        });
                    },
                    "取消": function () {
                        $("#dialog-role-form").dialog("close");
                    }
                }
            });
        });

        function loadRoleList() {
            $.ajax({
                url: "/sys/role/list.json",
                success: function (result) {
                    if (result.ret) {
                        var rendered = Mustache.render(roleListTemplate, {roleList: result.data});
                        $('#roleList').html(rendered);
                        bindRoleClick();
                        $.each(result.data, function (i, role) {
                            roleMap[role.id] = role;
                        });
                    } else {
                        showMessage("加载角色列表", result.msg, false);
                    }
                }
            });
        }

        function bindRoleClick() {
            // 处理点击选中
            $(".role-name").click(function (e) {
                e.preventDefault();
                e.stopPropagation(); // 此处必须要取消冒泡,因为是个递归结构,冒泡的话会让一个点击被响应多个
                var roleId = $(this).attr("data-id");
                if (roleId != lastRoleId) {
                    handleRoleSelected(roleId);
                }
            });
            // 处理点击[编辑角色]按钮
            $(".role-edit").click(function (e) {
                e.preventDefault();
                e.stopPropagation(); // 此处必须要取消冒泡,因为是个递归结构,冒泡的话会让一个点击被响应多个
                var roleId = $(this).attr("data-id"); // 选中的角色id
                $("#dialog-role-form").dialog({
                    modal: true,
                    title: "编辑角色",
                    open: function (event, ui) {
                        $("#roleForm")[0].reset();
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide(); // 点开时隐藏关闭按钮
                        var targetRole = roleMap[roleId];
                        if (targetRole) {
                            $("#roleId").val(targetRole.id);
                            $("#roleName").val(targetRole.name);
                            $("#roleRemark").val(targetRole.remark);
                        }
                    },
                    buttons: {
                        "更新": function (e) {
                            e.preventDefault();
                            updateRole(false, function (data) { // success callback
                                $("#dialog-role-form").dialog("close");
                            }, function (data) { // fail callback
                                showMessage("更新角色", data.msg, false);
                            });
                        },
                        "取消": function () {
                            $("#dialog-role-form").dialog("close");
                        }
                    }
                });
            });
            // 处理点击[删除角色]按钮
            $(".role-delete").click(function (e) {
                e.preventDefault();
                e.stopPropagation(); // 此处必须要取消冒泡,因为是个递归结构,冒泡的话会让一个点击被响应多个
                var roleId = $(this).attr("data-id");
                var roleName = $(this).attr("data-name");
                if (confirm("确定要删除角色[" + roleName + "]吗?")) {
                    $.ajax({
                        url: "/sys/role/delete.json",
                        data: {
                            id: roleId
                        },
                        success: function (result) {
                            if (result.ret) {
                                showMessage("删除角色[" + roleName + "]", "操作成功", true);
                                loadRoleList();
                            } else {
                                showMessage("删除角色[" + roleName + "]", result.msg, false);
                            }
                        }
                    });
                }
            });
        }

        // 选中一个权限模块, 加载样式, 并加载对应权限点的列表并渲染
        function handleRoleSelected(roleId) {
            if (lastRoleId != -1) {
                var lastRole = $("#role_" + lastRoleId + " .dd2-content:first");
                lastRole.removeClass("btn-yellow");
                lastRole.removeClass("no-hover");
            }
            var currentRole = $("#role_" + roleId + " .dd2-content:first");
            currentRole.addClass("btn-yellow");
            currentRole.addClass("no-hover");
            lastRoleId = roleId;
            $('#roleTab a:first').trigger('click');
            if(selectFirstTab) {
                loadRoleAcls(roleId);
            }
        }

        // 与后端交互, 新增或更新权限角色
        function updateRole(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/role/save.json" : "/sys/role/update.json",
                data: $("#roleForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        loadRoleList();
                        if (successCallback) {
                            successCallback(result); // 使用callback.call() 这种无法传递参数
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            });
        }
        function loadRoleAcls(selectRoleId) {
            $.ajax({
                url: "/sys/role/aclTree.json",
                data: {
                    roleId: selectRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        renderAclTree(result, "roleAclTree", true);
                    } else {
                        showMessage("加载角色权限数据", result.msg, false);
                    }
                }
            });
        }

        function loadRoleUsers(selectRoleId) {
            $.ajax({
                url: "/sys/role/users.json",
                data: {
                    roleId: selectRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                    } else {
                        showMessage("加载角色用户数据", result.msg, false);
                    }
                }
            });
        }

        // 绑定tab选中事件
        $('#roleTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            if(e.target.getAttribute("href") == '#roleAclTab') {
                selectFirstTab = true;
                loadRoleAcls(lastRoleId);
            } else {
                selectFirstTab = false;
                loadRoleUsers(lastRoleId);
            }
        });

        $(".saveRoleAcl").click(function(e) {
            e.preventDefault();
            $.ajax({
                url: '/sys/role/changeAcls.json?roleId=' + lastRoleId,
                data: $("#roleAclTree").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("保存角色与权限点关系", "操作成功", true);
                    } else {
                        showMessage("加载角色用户数据", result.msg, false);
                    }
                }
            });
        });
    });
</script>
</body>
</html>
