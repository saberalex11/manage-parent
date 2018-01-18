var $;
layui.config({
  base: "../../js/"
}).use(['form','layer','jquery'],function(){
  var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		$ = layui.jquery;

  //部门树
  $('#dept').click(function(){

    layui.layer.open({
      type: 1,
      offset: '50px',
      skin: 'layui-layer-molv',
      title: "选择部门",
      area: ['300px', '450px'],
      shade: 0,
      shadeClose: false,
      content:  $("#deptLayer"),
      btn: ['确定', '取消'],
      btn1: function (index) {
        var node = ztree.getSelectedNodes();
        //选择上级部门
        $('#deptId').val(node[0].deptId);
        $('#dept').val(node[0].name);

        layui.layer.close(index);
      }
    });

  });

  var addUser;
 	form.on("submit(addUser)",function(data){
    var userId = $('#userId').val();
    var url = "sys/user/save";

    addUser = '{"userAccount":"' + $('#userAccount').val() + '",';//工号
    addUser += '"userName":"' + $("#userName").val() + '",';  //用户名
    addUser += '"password":"' + $("#password").val() + '",';  //密码
    addUser += '"email":"' + $("#email").val() + '",';	 //邮箱
    addUser += '"mobile":"' + $("#mobile").val() + '",'; //手机号
    addUser += '"deptId":"' + $("#deptId").val() + '",'; //部门id
    addUser += '"isUseable":"' + $("#isUseable").find("option:selected").val() + '"'; //用户状态
    if (userId) {
      addUser += ',"userId":"' + userId + '"}';
      url = "sys/user/update";
    } else {
      addUser += '}';
    }

 		//弹出loading
 		var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

    $.ajax({
      type: "POST",
      url: baseURL + url,
      contentType: "application/json",
      data: addUser,//JSON.stringify(
      success: function (r) {
        if (r.code === 0) {
          top.layer.close(index);
          //top.layer.msg("用户添加成功！");
          alert('操作成功', function () {
            layer.closeAll("iframe");
            //刷新父页面
            parent.reload();
          });
        } else {
          top.layer.close(index);
          layer.msg(r.msg);
        }
      }
    });
 		return false;
  });

  $('#cancle').click(function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index);
  });


});

var setting = {
  data: {
    simpleData: {
      enable: true,
      idKey: "deptId",
      pIdKey: "parentId",
      rootPId: -1
    },
    key: {
      url:"nourl"
    }
  }
};
var ztree;

var getDept = function (deptId) {
  //加载部门树
  $.get(baseURL + "sys/dept/select", function (r) {
    ztree = $.fn.zTree.init($("#deptTree"), setting, r.data);
    var node = ztree.getNodeByParam("deptId", deptId);
    ztree.selectNode(node);

    $('#dept').val(node.name);

  })
};
