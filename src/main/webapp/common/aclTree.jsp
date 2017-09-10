<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script id="aclTreeTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#aclModuleList}}
        <li class="aclModule-name" id="aclModuleTree_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <span class="green"><i class="ace-icon fa fa-angle-double-down bigger-120 sub-tree"></i></span>&nbsp;
            <b>{{name}}</b> &nbsp; <input type="checkbox" class="aclModuleIds {{#showCheckbox}}{{/showCheckbox}}" value="{{id}}" />
            <ol class="dd-list " style="margin-left : 10px;">
                {{#aclList}}
                    &nbsp;
                    <span>
                    <input type="checkbox" class="aclIds {{#showCheckbox}}{{/showCheckbox}}" name="aclIds" value="{{id}}"
                    {{#checked}}checked="checked"{{/checked}} {{#readonly}}{{/readonly}} />
                    {{name}}{{#showMenu}}{{/showMenu}}&nbsp;
                    </span> &nbsp;&nbsp;
                {{/aclList}}
            </ol>
        </li>
    {{/aclModuleList}}
</ol>
</script>

<script type="text/javascript">
    var aclTreeTemplate = $('#aclTreeTemplate').html();
    Mustache.parse(aclTreeTemplate);

    function renderAclTree(result, idElement, showCheckbox) {
        var rendered = Mustache.render(aclTreeTemplate, {
            aclModuleList: result.data,
            "showCheckbox": function () { // 对展示做特殊处理
                return function (text, render) {
                    return showCheckbox ? "" : "hidden";
                }
            }
        });
        $('#' + idElement).html(rendered);
        recursiveRenderAclModule(result.data, showCheckbox);
        bindTreeClick();
    }
    // 递归渲染权限模块列表
    function recursiveRenderAclModule(aclModuleList, showCheckbox) {
        if (aclModuleList && aclModuleList.length > 0) {
            $(aclModuleList).each(function (i, aclModule) {
                if (aclModule.aclModuleList && aclModule.aclModuleList.length > 0) {
                    var rendered = Mustache.render(aclTreeTemplate, {
                        aclModuleList: aclModule.aclModuleList,
                        "readonly": function() {
                            return this.hasAcl ? "" : "readonly";
                        },
                        "showCheckbox": function () { // 对展示做特殊处理
                            return function (text, render) {
                                return showCheckbox ? "" : "hidden";
                            }
                        },
                        "showMenu": function() {
                            return function (text, render) {
                                return this.type == 0 ? "<span style='color:#1b6aaa;'>(菜单)</span>" : "";
                            }
                        }
                    });
                    $('#aclModuleTree_' + aclModule.id).append(rendered);
                    recursiveRenderAclModule(aclModule.aclModuleList, showCheckbox);
                }
            });
        }
    }

    function bindTreeClick() {
        $(".aclModuleIds").click(function() {
            if($(this).attr("checked") == 'checked' || $(this).attr("checked") == true) {
                $(this).parent().find(".aclModuleIds,.aclIds").removeAttr("checked");
            } else {
                // 同时使用attr和prop可以多次选中取消后checkout依旧能正确响应,否则总有bug
                $(this).parent().find(".aclModuleIds,.aclIds").attr("checked", true).prop("checked", true);
            }
        });
    }
</script>
