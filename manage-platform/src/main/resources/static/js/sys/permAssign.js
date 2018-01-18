//菜单树
var menu_ztree;
var menu_setting = {
  data: {
    simpleData: {
      enable: true,
      idKey: "menuId",
      pIdKey: "parentId",
      rootPId: -1
    },
    key: {
      url:"nourl"
    }
  },
  check:{
    enable:true,
    nocheckInherit:true
  }
};
//部门树初始化设置
var dept_ztree;
var dept_setting = {
  data: {
    simpleData: {
      enable: true,
      idKey: "deptId",
      pIdKey: "parentId",
      rootPId: -1
    },
    key: {
      url: "nourl"
    }
  },
  check: {
    enable: true,
    //nocheckInherit:true,
    chkStyle: "radio",
    radioType: "all"
  },
  callback: {
    beforeCheck: function (treeId, treeNode) {
      return !treeNode.isParent;
    },
    onCheck: function (event, treeId, treeNode) {
      var deptId = treeNode.deptId;
      $('#deptId').val(deptId);
      $('#deptName').text(treeNode.name);
      /**初始化角色列表**/
      deptRoleList(deptId);
    }
  }
};

var getRole = function (roleId) {
  $.get(baseURL + "sys/role/info/" + roleId, function (r) {
    //vm.role = r.role;
    //勾选角色所拥有的菜单
    var menuIds = r.role.menuIdList;
    //取消勾选所有节点
    menu_ztree.checkAllNodes(false);
    for (var i = 0; i < menuIds.length; i++) {
      var node = menu_ztree.getNodeByParam("menuId", menuIds[i]);
      menu_ztree.checkNode(node, true, false);
    }

  });

};
var getMenuTree = function () {
  //加载菜单树
  $.get(baseURL + "sys/menu/list", function (r) {

    menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r.data);
    //展开所有节点
    menu_ztree.expandAll(true);

  });
}

layui.config({
	base : "../../js/"
}).use(['layer', 'element', 'table'], function () {
  var layer = parent.layer === undefined ? layui.layer : parent.layer,
      element = layui.element, //Tab的切换功能，切换事件监听等，需要依赖element模块
      table = layui.table,
      $ = layui.jquery;

  //权限判断
  if(hasPermission("sys:permAssign:roleMenuSave")){
    $('#roleMenuSave').removeClass('layui-hide');
  }
  if(hasPermission("sys:permAssign:deptRoleSave")){
    $('#deptRoleSave').removeClass('layui-hide');
  }
  if(hasPermission("sys:permAssign:userRoleSave")){
    $('#userRoleSave').removeClass('layui-hide');
  }

  /**角色表格加载*/
  var roleTableRender = table.render({
    elem: '#roleTableList',
    url: baseURL + 'sys/role/list',
    id:'roleTableId',
    method: 'post',
    height:'460',
    skin:'row',
    even:'true',
    loading: 'true',//是否显示加载条
    //size: 'sm',
    cols: [[
      {checkbox: true, event: 'click'},
      {field:'roleId', title: 'ID',width:80, sort: true,event:'click'},
        {field:'roleType', title: '角色类别',width: 120 ,event:'click',templet: '#roleTpl'},
      {field:'roleName', title: '角色名称',width: 120 ,event:'click'},
      {field:'deptName', title: '角色所属部门',width: 150,event:'click'},
      {field:'remark', title: '角色说明',width: 150,event:'click'},
      {field:'createTime', title: '创建时间',width: 150,event:'click'}
      //{fixed:'right', title: '操作', align:'center',width: 195, toolbar: '#roleBar'}
    ]],
    page: true,
    limit: 10 ,//默认显示10条
    done: function(res, curr, count){
      //如果是异步请求数据方式，res即为你接口返回的信息。
      //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度

      //加载菜单树
      getMenuTree();

    }
  });

  //监听表格的点击事件 角色资源分配角色列表点击事件
  table.on('tool(roleTable)', function(obj) {

    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值

    if(layEvent =='click'){

      $('#menuTree').removeClass("layui-hide");
      $('#roleName').text(data.roleName);
      $('#roleId').val(data.roleId);
      var roleId = data.roleId ;
      //layer.alert("当前行点击事件tool：" + JSON.stringify(data));
      if (roleId != null) {
        getRole(roleId);
      }

    }

  });

  //监听表格的点击事件 用户表格的点击事件  加载用户角色列表
  table.on('tool(userTable)', function (obj) {

    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值

    if (layEvent == 'click') {

      var userId = data.userId;
      $('#userName').text(data.userName);
      $('#userId').val(userId);
      //根据用户的id先查询用户所属部门，如果不存在则提示先选择部门
      //在查询该用户可分配的角色 即所属部门下的角色
      //超级管理员如何处理
      //if (userId != null) {
      $.get(baseURL + "sys/user/info/" + userId, function (r) {
        var deptId = r.data.deptId;
        //if (deptId){
        /**用户角色表格加载*/
        var userRoleTableRender = table.render({
          elem: '#userRoleTableList',
          url: baseURL + 'sys/role/queryByDeptId',
          id: 'userRoleTableId',
          method: 'post',
          height: '460',
          skin: 'row',
          even: 'true',
          loading: 'true',//是否显示加载条
          //size: 'sm',
          cols: [[
            {checkbox: true, event: 'click'},
            {field: 'roleId', title: 'ID', width: 80, sort: true, event: 'click'},
              {field:'roleType', title: '角色类别',width: 120 ,event:'click',templet: '#roleTpl'},
            {field: 'roleName', title: '角色名称', width: 180, event: 'click'},
            {field: 'remark', title: '角色说明', width: 150, event: 'click'},
            {field: 'createTime', title: '创建时间', width: 150, event: 'click'}
            //{fixed:'right', title: '操作', align:'center',width: 195, toolbar: '#roleBar'}
          ]],
          page: false,
           limit: 9999, //默认显示10条
          where: {deptId: deptId,"userId":userId}
        });

      });

    }

  });

  /**监听tab页面的点击事件**/
  element.on('tab(assign)', function (data) {
    var tabIndex = data.index;
    /**点击部门角色分配页面**/
    if (tabIndex == 1) {
      /**初始化部门树 和角色列表 **/
      $.get(baseURL + "sys/dept/select", function (r) {

        dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r.data);
        //展开所有节点
        dept_ztree.expandAll(true);

      });
    }
    /**点击用户角色分配页面**/
    if (tabIndex == 2) {
      /**用户表格加载*/
      window.userTableRender = table.render({
        elem: '#userTableList',
        url: baseURL + 'sys/user/list',
        id: 'userTableId',
        method: 'post',
        height: '460',
        skin: 'row',
        even: 'true',
        loading: 'true',//是否显示加载条
        //size: 'sm',
        cols: [[
          {checkbox: true, event: 'click'},
          {field: 'userAccount', title: '工号', width: 80, sort: true, event: 'click'},
          {field: 'userName', title: '用户名', width: 150, event: 'click'},
          {field: 'remark', title: '手机号', width: 150, event: 'click'},
          {field: 'status', title: '状态', width: 80, templet: '#userStatusTpl', event: 'click'},
          {field: 'createTime', title: '创建时间', width: 150, event: 'click'}
        ]],
        page: true,
        limit: 10,//默认显示10条
        done: function (res, curr, count) {
          //如果是异步请求数据方式，res即为你接口返回的信息。
          //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度

          //加载角色列表
          //vm.getMenuTree();

        }
      });
    }

  });

  //加载部门角色列表
  window.deptRoleList = function (deptId) {
    /**部门角色表格加载*/
    var deptRoleTableRender = table.render({
      elem: '#deptRoleTableList',
      url: baseURL + 'sys/role/list',
      id: 'deptRoleTableId',
      method: 'post',
      height: '460',
      skin: 'row',
      even: 'true',
      loading: 'true',//是否显示加载条
      //size: 'sm',
      cols: [[
        {checkbox: true},
        {field: 'roleId', title: 'ID', width: 80, sort: true, event: 'click'},
        {field: 'roleName', title: '角色名称', width: 180, event: 'click'},
        {field: 'remark', title: '角色说明', width: 150, event: 'click'},
        {field: 'createTime', title: '创建时间', width: 150, event: 'click'}
        //{fixed:'right', title: '操作', align:'center',width: 195, toolbar: '#roleBar'}
      ]],
      page: false,
      limit: 9999 ,//默认显示10条
      where: {deptId: deptId}
    });

  };

  //角色资源分配查询
    $("#roleSearch").click(function(){
        roleTableRender.reload({
            where: {
                'roleName': $("#roleSearchName").val()
              }
        });
    });
    $("#roleReset").click(function(){
        $("#roleSearchName").val("");
    });

    //角色用户分配查询
    $("#userSearch").click(function(){
        userTableRender.reload({
            where: {
                'userAccount': $("#userSearchAccount").val()
            }
        });
    });
    $("#userReset").click(function(){
        $("#userSearchAccount").val("");
    });


  //保存角色资源
  $('#roleMenuSave').click(function(){
    //获取选择的菜单
    var nodes = menu_ztree.getCheckedNodes(true);
    var menuIdList = new Array();
    for(var i=0; i<nodes.length; i++) {
      menuIdList.push(nodes[i].menuId);
    }

    var url = 'sys/permAssign/roleMenuSave';
    $.ajax({
      type: "POST",
      url: baseURL + url,
      contentType: "application/json",
      data: JSON.stringify({'roleId':$('#roleId').val(),'menuIdList':menuIdList}),
      success: function(r){
        if(r.code === 0){
          alert('操作成功', function(){});
        }else{
          alert(r.msg);
        }
      }
    });


  });

  //保存部门角色关系
  $('#deptRoleSave').click(function(){

    //获取勾选的角色数据
    var checkStatus = table.checkStatus('deptRoleTableId'); //deptRoleTableId即为基础参数id对应的值
    var data = checkStatus.data;
    if (data.length == 0) {
      layer.msg("请选择需要分配的角色");
      return;
    }
    var roleIdList = [];
    $.each(data, function (index, item) {
      roleIdList.push(item.roleId);
    });

    var url = 'sys/permAssign/deptRoleSave';
    $.ajax({
      type: "POST",
      url: baseURL + url,
      contentType: "application/json",
      data: JSON.stringify({'deptId':$('#deptId').val(),'roleIdList':roleIdList}),
      success: function(r){
        if(r.code === 0){
          alert('操作成功', function(){});
        }else{
          alert(r.msg);
        }
      }
    });

  });

  //保存用户角色关系
  $('#userRoleSave').click(function(){
    //获取角色列表中选中的数据
    var checkStatus = table.checkStatus('userRoleTableId'); //userTableId即为基础参数id对应的值
    var data = checkStatus.data;
    if (data.length == 0) {
      layer.msg("请选择需要分配的角色");
      return;
    }
    var roleIdList = [];
    $.each(data, function (index, item) {
      roleIdList.push(item.roleId);
    });


    var url = 'sys/permAssign/userRoleSave';
    $.ajax({
      type: "POST",
      url: baseURL + url,
      contentType: "application/json",
      data: JSON.stringify({'userId':$('#userId').val(),'roleIdList':roleIdList}),
      success: function(r){
        if(r.code === 0){
          alert('操作成功', function(){});
        }else{
          alert(r.msg);
        }
      }
    });

  });


});
