/**
 * Created by HNAP on 2017-09-05.
 */
layui.config({
  base: "../../js/"
}).use(['form', 'layer'], function () {
  var $ = layui.jquery,
      form = layui.form,
      layer = layui.layer;
  //权限判断
  if(hasPermission("sys:menu:save")){
    $('#menuAdd_btn').removeClass('layui-hide');
  }
  /**加载菜单列表信息*/
  var colunms = Menu.initColumn();
  var table = new TreeTable(Menu.id, baseURL + "sys/menu/list", colunms);
  table.setExpandColumn(2);
  table.setIdField("menuId");
  table.setCodeField("menuId");
  table.setParentCodeField("parentId");
  table.setExpandAll(false);
  table.setStriped(true);//开启隔行变色效果
  table.init();
  Menu.table = table;

  /**菜单新增**/
  $('#menuAdd_btn').click(function(){

      vm.showList = false;
      vm.title = "新增";

      vm.getMenu();
      //获取选中的行
      var selected = $('#menuTable').bootstrapTreeTable('getSelections');
      if (selected.length == 0) {
      } else {
        vm.menu.parentName = selected[0].name;
      }

  });

  /**全选*/
  form.on('checkbox(allChoose)', function (data) {
    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
    child.each(function (index, item) {
      item.checked = data.elem.checked;
    });
    form.render('checkbox');
  });

  /**取消*/
  $("#cancle").click(function(){
    vm.showList = true ;
  });

  /**点击选择父级菜单*/
  $('#parentId').click(function () {
    layui.layer.open({
      type: 1,
      offset: '50px',
      skin: 'layui-layer-molv',
      title: "选择父级菜单",
      area: ['300px', '450px'],
      shade: 0,
      shadeClose: false,
      content: $("#menuLayer"),
      btn: ['确定', '取消'],
      btn1: function (index) {
        var node = ztree.getSelectedNodes();
        //选择上级菜单
        vm.menu.parentId = node[0].menuId;
        vm.menu.parentName = node[0].name;

        layui.layer.close(index);
      }
    });

  });

  /**选择图标*/
  $("#select_img").click(function () {
    var index = layui.layer.open({
      title: "图标选择",
      type: 2,
      skin: 'layui-layer-molv',
      width: '685px',
      height: '685px',
      content: "menuImg.html",
      success: function (layero, index) {
        setTimeout(function () {
          layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
            tips: 3
          });
        }, 500);
      }
    });

  });

  /**监听菜单类型选择*/
  form.on('select(menuTypeFilter)', function (data) {

    var type = $("#menuType option:selected").val();
    vm.menu.type = type;

    //if (type == 1){
    //  $('#menuLevel').removeAttr("disabled");
    //}else {
    //  $('#menuLevel').attr("disabled");
    //}
    //form.render('select','menuType');

  });

  /**表单验证*/
  form.verify({
    name: function (value, item) {
      //验证菜单名称
      if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5]+$").test(value)) {
        return '菜单名称只能为中文数字或者字母';
      }

    },
    parentId: function (value, item) {
      //验证父级菜单
      var menuLevel = $("#menuLevel").val();
      if ((menuLevel == 2 || menuLevel == 3) && value == '') {
        return '父级菜单不能为空';
      }
    },
    url: function (value, item) {
      //验证菜单路径
      var menuLevel = $("#menuLevel").val();
      if ((menuLevel == 2 || menuLevel == 3) && value == '') {
        return '菜单路径不能为空';
      }
      if (value != '' && !new RegExp("^[a-zA-Z_/.]+$").test(value)) {
        return '菜单路径只能为英文下划线斜杠和点';
      }

    },
    orderNum: function (value, item) {
      //验证排序
      if (value != '' && !new RegExp("^[0-9]*$").test(value)) {
        return '排序只能为数字';
      }

    }
  });


  /**保存菜单资源信息*/
  form.on("submit(saveMenu)", function (data) {
    var resSaveLoading = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
    var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
    vm.menu.type = $("#menuType option:selected").val();
    vm.menu.level = $("#menuLevel option:selected").val();

    $.ajax({
      url: baseURL + url,
      type: 'POST',
      async: false,
      contentType: "application/json",
      data: JSON.stringify(vm.menu),
      success: function (r) {
        if (r.code == 0) {
          //数据初始化
          vm.menu = {parentName:null, parentId:0, orderNum:0 }
          top.layer.close(resSaveLoading);
          layui.layer.msg("操作成功");
          var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
          parent.layer.close(index); //再执行关闭
          vm.reload();
        } else {
          top.layer.close(resSaveLoading);
          layui.layer.msg(r.msg);
        }
      }, error: function (data) {
        top.layer.close(resSaveLoading);
        top.layer.close(index);
      }
    });
    return false;
  })

});//layui config结束


/**undefined 值 过滤*/
function objNull(obj) {
  if(typeof(obj) == "undefined" || obj == null){
    return "";
  }
  return obj;
}

var setting = {
  data: {
    simpleData: {
      enable: true,
      idKey: "menuId",
      pIdKey: "parentId",
      rootPId: -1
    },
    key: {
      url: "nourl"
    }
  }
};
var ztree;

//vue初始化
var vm = new Vue({
  el:'#dqapp',
  data:{
    showList: true,
    title: null,
    menu:{
      parentName:null,
      parentId:0,
      orderNum:0
    }
  },
  methods: {
    getMenu: function () {
      //加载菜单树
      $.get(baseURL + "sys/menu/select", function (r) {
        ztree = $.fn.zTree.init($("#menuTree"), setting, r.data);
        var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
        ztree.selectNode(node);

        vm.menu.parentName = node.name;

      })
    },
    reload: function () {
      vm.showList = true;
      Menu.table.refresh();
    }
  }
});

var Menu = {
  id: "menuTable",
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
  var columns = [
    {field: 'selectItem', radio: true},
    {title: '菜单ID', field: 'menuId', visible: false, align: 'center', valign: 'middle', width: '80px'},
    {title: '菜单名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '180px'},
    {title: '上级菜单', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
    {title: '菜单路径', field: 'url', align: 'center', valign: 'middle', sortable: true, width: '100px'},
    {title: '菜单权限', field: 'perms', align: 'center', valign: 'middle', sortable: true, width: '100px'},
    {
      title: '菜单类型',
      field: 'type',
      align: 'center',
      valign: 'middle',
      sortable: true,
      width: '100px',
      formatter: function (item, index) {
        if (item.type === 1) {
          return '<span class="label label-success">菜单</span>';
        }
        if (item.type === 2) {
          return '<span class="label label-warning">按钮</span>';
        }
      }
    },
    {
      title: '菜单级别',
      field: 'level',
      align: 'center',
      valign: 'middle',
      sortable: true,
      width: '100px',
      formatter: function (item, index) {
        if (item.level === 1) {
          return '<span class="label label-primary">一级菜单</span>';
        }
        if (item.level === 2) {
          return '<span class="label label-success">二级菜单</span>';
        }
        if (item.level === 3) {
          return '<span class="label label-warning">三级菜单</span>';
        }
      }
    },
    {
      title: '菜单图标',
      field: 'icon',
      align: 'center',
      valign: 'middle',
      sortable: true,
      width: '100px',
      formatter: function (item, index) {
        return item.icon == null ? '' : '<i class="layui-icon ' + item.icon + '"></i>';
      }
    },
    {
      title: '操作', align: 'center', valign: 'middle', sortable: true, width: '100px', formatter: function(row, index){return operationFormatter (row, index)}
    }
  ];
  return columns;
};

/**生成编辑和删除菜单**/
function operationFormatter(row, index){
  var str = "" ;
  if (hasPermission("sys:menu:update")){
    str = str + '<div class="layui-btn-group"><a class="layui-btn layui-btn-mini" onclick="menuEdit(' + row.menuId + ')"><i class="layui-icon icon-font larry-bianji2"></i> 编辑</a>';
  }
  if (hasPermission("sys:menu:delete")){
    str = str + '<a class="layui-btn layui-btn-mini layui-btn-danger" onclick="menuDel(' + row.menuId + ')"><i class="layui-icon icon-font larry-ttpodicon"></i>删除</a></div>';
  }
  return str;
}

/**菜单编辑**/
function menuEdit(menuId) {

  if (menuId == null || !menuId) {
    return;
  }

  $.get(baseURL + "sys/menu/info/" + menuId, function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.menu = r.data;

    /*layui方式的选中select*/
    $("#menuType").next().find("dd[lay-value='"+vm.menu.type+"']").click();
    $("#menuLevel").next().find("dd[lay-value='"+vm.menu.level+"']").click();

    vm.getMenu();
  });

}
/**菜单删除**/
function menuDel(menuId) {

  if (menuId == null || !menuId) {
    return;
  }

  confirm('确定要删除选中的记录？', function () {
    $.ajax({
      type: "POST",
      url: baseURL + "sys/menu/delete",
      data: "menuId=" + menuId,
      success: function (r) {
        if (r.code == 0) {
          alert('操作成功', function () {
            vm.reload();
          });
        } else {
          alert(r.msg);
        }
      }
    });
  });

}