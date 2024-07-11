/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var PollutionTypeInfoDlg = {
    pollutionTypeInfoData: {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '用户名不能为空'
                }
            }
        },
        unit: {
            validators: {
                notEmpty: {
                    message: '单位不能为空'
                }
            }
        },
        limitvalue: {
            validators: {
                notEmpty: {
                    message: '阈值不能为空'
                }
            },
            numeric: {
                message: '请输入合法的浮点数'
            }
        },
    }
};

/**
 * 清除数据
 */
PollutionTypeInfoDlg.clearData = function () {
    this.pollutionTypeInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PollutionTypeInfoDlg.set = function (key, val) {
    this.pollutionTypeInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PollutionTypeInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
PollutionTypeInfoDlg.close = function () {
    parent.layer.close(window.parent.PollutionType.layerIndex);
};



/**
 * 收集数据
 */
PollutionTypeInfoDlg.collectData = function () {
    this.set('id').set('name').set('unit').set('limitvalue');
};

/**
 * 验证数据是否为空
 */
PollutionTypeInfoDlg.validate = function () {
    $('#pollutionTypeInfoForm').data("bootstrapValidator").resetForm();
    $('#pollutionTypeInfoForm').bootstrapValidator('validate');
    return $("#pollutionTypeInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
PollutionTypeInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pollution/type/add", function (data) {
        Feng.success("添加成功!");
        window.parent.PollutionType.table.refresh();
        PollutionTypeInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    // 获取pollutionTypeInfoData的name属性的值
    ajax.set(this.pollutionTypeInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
PollutionTypeInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pollution/type/update", function (data) {
        Feng.success("修改成功!");
        window.parent.PollutionType.table.refresh();
        PollutionTypeInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pollutionTypeInfoData);
    ajax.start();
};

/**
 * 初始化表格的列
 */
PollutionTypeInfoDlg.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: false},
        {title: '单位', field: 'unit', align: 'center', valign: 'middle', sortable: false},
        {title: '阈值', field: 'limitvalue', align: 'center', valign: 'middle', sortable: true}
    ]
    return columns;
};

$(function () {
    Feng.initValidator("pollutionTypeInfoForm", PollutionTypeInfoDlg.validateFields);
    var defaultColunms = PollutionTypeInfoDlg.initColumn();
    var table = new BSTable(PollutionType.id, "/pollution/type/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    PollutionType.table = table;
});

