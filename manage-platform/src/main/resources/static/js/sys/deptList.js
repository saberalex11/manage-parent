layui.config({
  base : "js/"
}).use(['layer', 'jquery'], function () {
  var layer = parent.layer === undefined ? layui.layer : parent.layer,
      $ = layui.jquery;

  //权限判断
  if(hasPermission("sys:dept:save")){
    $('#deptAdd_btn').removeClass('layui-hide');
  }

  /**加载部门树表格**/
  $.get(baseURL + "sys/dept/info", function (r) {
    var colunms = Dept.initColumn();
    var table = new TreeTable(Dept.id, baseURL + "sys/dept/list", colunms);
    table.setRootCodeValue(r.deptId);
    table.setExpandColumn(2);
    table.setIdField("deptId");
    table.setCodeField("deptId");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.setStriped(true);//开启各行变色效果
    table.init();
    Dept.table = table;
  });

  //部门树
  $('#deptParent').click(function(){

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
        vm.dept.parentId = node[0].deptId;
        vm.dept.parentName = node[0].name;

        layui.layer.close(index);
      }
    });

  });



});//layui的use结束

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

var vm = new Vue({
  el:'#dqapp',
  data:{
    showList: true,
    title: null,
    dept:{
      parentName:null,
      parentId:0,
      orderNum:0
    },
    queryCondition:null
  },
  methods: {
    getDept: function () {
      //加载部门树
      $.get(baseURL + "sys/dept/select", function (r) {
        ztree = $.fn.zTree.init($("#deptTree"), setting, r.data);
        var node = ztree.getNodeByParam("deptId", vm.dept.parentId);
        ztree.selectNode(node);

        vm.dept.parentName = node.name;

      })
    },
    add: function () {
      vm.showList = false;
      vm.title = "新增";
      vm.dept = {parentName: null, parentId: 0, orderNum: 0};
      vm.getDept();
    },
    saveOrUpdate: function (event) {
      var url = vm.dept.deptId == null ? "sys/dept/save" : "sys/dept/update";
      $.ajax({
        type: "POST",
        url: baseURL + url,
        contentType: "application/json",
        data: JSON.stringify(vm.dept),
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功', function () {
              vm.reload();
            });
          } else {
            alert(r.msg);
          }
        }
      });
    },
    reload: function () {
      vm.showList = true;
      Dept.table.refresh();
    }
  }
});

var Dept = {
  id: "deptTable",
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
  var columns = [
    {field: 'selectItem', radio: true},
    {title: '部门ID', field: 'deptId', visible: false, align: 'center', valign: 'middle', width: '80px'},
    {title: '部门名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '185px'},
    {title: '上级部门', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '185px'},
    {title: '操作人', field: 'operator', align: 'center', valign: 'middle', sortable: true, width: '90px'},
    {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true, width: '100px'},
    {title: '更新时间', field: 'updateTime', align: 'center', valign: 'middle', sortable: true, width: '100px'},
    {title: '状态', field: 'status', align: 'center', valign: 'middle', sortable: true, width: '50px',
      formatter: statusFormatter},
    {
      title: '操作', align: 'center', valign: 'middle', sortable: true, width: '100px', formatter: function (row, index) {
      return operationFormatter(row, index);}
    }
  ];
  return columns;
};

/**状态格式化显示**/
function statusFormatter(row, index){
  if(row.status == 1) return '<span style="color: #0f26f5;">正常</span>';
  if(row.status == 0) return '<span style="color: #dd2420;">删除</span>';
}


/**生成编辑和删除菜单**/
function operationFormatter(row, index){
  var str = "" ;
  if (hasPermission("sys:dept:update")){
    str = str + '<div class="layui-btn-group"><a class="layui-btn layui-btn-mini" onclick="deptEdit(' + row.deptId + ')"><i class="layui-icon icon-font larry-bianji2"></i> 编辑</a>';
  }
  if (hasPermission("sys:dept:delete")){
    str = str + '<a class="layui-btn layui-btn-mini layui-btn-danger" onclick="deptDel(' + row.deptId + ')"><i class="layui-icon icon-font larry-ttpodicon"></i>删除</a></div>';
  }
  return str;
}

/**部门编辑**/
function deptEdit(deptId) {
  if (deptId == null || !deptId) {
    return;
  }

  $.get(baseURL + "sys/dept/info/" + deptId, function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.dept = r.dept;

    vm.getDept();
  });
}
/**部门删除**/
function deptDel(deptId) {

  if (deptId == null || !deptId) {
    return;
  }

  confirm('确定要删除选中的记录？', function () {
    $.ajax({
      type: "POST",
      url: baseURL + "sys/dept/delete",
      data: "deptId=" + deptId,
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