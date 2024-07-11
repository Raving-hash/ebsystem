/**
 * 角色管理的单例
 */
var Application = {
    id: "applicationTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Application.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '申请类型', field: 'typeName', align: 'center', valign: 'middle', sortable: false},
        {title: '设备名', field: 'deviceName', align: 'center', valign: 'middle', sortable: false},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: false},
        {title: '申请人', field: 'userName', align: 'center', valign: 'middle', sortable: false},
        {title: '审核人', field: 'auditUserName', align: 'center', valign: 'middle', sortable: false},
        {title: '申请时间', field: 'applyTime', align: 'center', valign: 'middle', sortable: true},
        {title: '审核时间', field: 'auditTime', align: 'center', valign: 'middle', sortable: true},
        {title: '修改时间', field: 'editTime', align: 'center', valign: 'middle', sortable: true},
    ];
};


/**
 * 检查是否选中
 */
Application.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length === 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Application.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加污染类型
 */
Application.openAddApplication = function () {
    this.layerIndex = layer.open({
        type: 2,
        title: '添加申请',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/application/getAdd'
    });
};

/**
 * 点击修改按钮时
 */
Application.openChangeApplication = function () {
    if (this.check()) {
        this.layerIndex = layer.open({
            type: 2,
            title: '修改申请',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/application/getEdit/' + this.seItem.id
        });
    }
};

/**
 * 删除申请
 */
Application.delApplication = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/application/delete", function () {
                Feng.success("删除成功!");
                Application.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("applicationId", Application.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否取消申请 " + Application.seItem.id + "?",operation);
    }
};

Application.search = function () {
    var queryData = {};
    queryData['user'] = $("#user").val();
    queryData['deviceId'] = $("#deviceId").val();
    queryData['status'] = $("#status").val();
    queryData['type'] = $("#type").val();
    queryData['auditUser'] = $("#auditUser").val();
    Application.table.refresh({query: queryData});
}

Application.acceptApplication = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/application/audit", function () {
                Feng.success("审核成功!");
                Application.table.refresh();
            }, function (data) {
                Feng.error("审核失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", Application.seItem.id);
            ajax.set("status", "1");
            ajax.start();
        };

        Feng.confirm("是否审核通过 " + Application.seItem.id + "?",operation);
    }
}

Application.refuseApplication = function () {
    if (this.check()) {

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/application/audit", function () {
                Feng.success("审核成功!");
                Application.table.refresh();
            }, function (data) {
                Feng.error("审核失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", Application.seItem.id);
            ajax.set("status", "2");
            ajax.start();
        };

        Feng.confirm("是否审核不通过 " + Application.seItem.id + "?", operation);
    }
}


$(function () {
    var defaultColunms = Application.initColumn();
    var table = new BSTable(Application.id, "/application/search", defaultColunms);
    table.setPaginationType("client");
    table.init();
    Application.table = table;
});
 