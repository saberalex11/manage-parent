layui.config({
  base: "../../js/"
}).use(['form', 'table', 'layer'], function () {
  var $ =  layui.$,
      form = layui.form,
      table = layui.table,
      layer = layui.layer;

  //权限判断
  if(hasPermission("sys:role:save")){
    $('#roleAdd').removeClass('layui-hide');
  }

  /**角色表格加载*/
  var roleTableRender = table.render({
    elem: '#roleTableList',
    url: baseURL + 'sys/role/list',
    id:'roleTableId',
    method: 'post',
    height:'480',
    skin:'row',
    even:'true',
//            size: 'sm',
    cols: [[
      {checkbox: true,}, //fixed:'left',
      {field: 'roleName', title: '角色名称', width: 120},
      {field: 'remark', title: '角色描述', width: 150},
      {field: 'createId', title: '操作人', width: 150},
      {field: 'createTime', title: '创建时间', width: 200},
      {field: 'updateTime', title: '更新时间', width: 200},
      {field: 'isUseable', title: '状态', width: 200, templet: '#roleStatusTpl'},
      {fixed: 'right', title: '操作', align: 'center', width: 195, toolbar: '#roleBar'}
    ]],
    page: true,
    limit: 10//默认显示10条
  });
  /**表格重载**/
  reloadRoleTable = function (roleName) {
    roleTableRender.reload({
      where: {
        'roleName': roleName
      }
    });
  };

  /**监听查询提交*/
  form.on('submit(roleSearchFilter)',function (data) {
    console.info("字段數據：："+data.field.searchContent);
    reloadRoleTable(data.field.searchContent);
  });

  /**角色新增*/
  $("#roleAdd").click(function () {

    var index = layui.layer.open({
      title : "新增角色",
      type : 2,
      skin : 'layui-layer-molv',
      content : "roleAdd.html",
      success : function(layero, index){
        setTimeout(function(){
          layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
            tips: 3
          });
        },500)
      }
    })
    //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
    $(window).resize(function(){
      layui.layer.full(index);
    })
    layui.layer.full(index);

  });

  /**监听工具条*/
  table.on('tool(roleTable)', function(obj) {

    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值
    //修改角色
    if (layEvent === 'roleEdit') {
      var index = layui.layer.open({
        title : "编辑角色",
        type : 2,
        skin : 'layui-layer-molv',
        content : "roleAdd.html",
        success : function(layero, cIndex){
          //数据渲染
          var body = layui.layer.getChildFrame('body',cIndex);//获得子页面的dom元素

          body.find('#roleId').val(data.roleId);
          body.find('#roleName').val(data.roleName);
          body.find('#remark').val(data.remark);

          setTimeout(function(){
            layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
              tips: 3
            });
          },500);

        }
      })
      //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
      $(window).resize(function(){
        layui.layer.full(index);
      })
      layui.layer.full(index);

      //角色删除
    } else if (layEvent === 'roleDel') {
      var roleId = data.roleId;
      var status = data.status;
      if (status == 0) {
        layui.layer.msg("当前角色已失效");
        return false;
      }

      var param = [roleId];
      layui.layer.confirm('确定要删除选中的记录？', function (index) {
        $.ajax({
          type: "POST",
          url: baseURL + 'sys/role/delete',
          contentType: "application/json",
          data: JSON.stringify(param),
          success: function (r) {
            if (r.code === 0) {
              layui.layer.close(index);
              top.layer.msg("角色删除成功！");
              //刷新父页面
              reloadRoleTable();
            } else {
              layui.layer.close(index);
              layer.msg(r.msg);
            }
          }
        });

      });

    }

  });
});
