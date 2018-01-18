layui.config({
  base: "../../js/"
}).use(['form', 'layer', 'jquery', 'table'], function () {
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
    table = layui.table,
		$ = layui.jquery;

  //权限判断
  if(hasPermission("sys:user:save")){
    $('#userAdd').removeClass('layui-hide');
  }
  if(hasPermission("sys:user:delete")){
    $('#userDelBatch').removeClass('layui-hide');
  }

  /**用户表格加载*/
  var userTableRender = table.render({
    elem: '#userTableList',
    url: baseURL +"sys/user/list",
    id:'userTableId',
    method: 'post',
    height:'500',
    skin:'row',
    loading:'true',//是否显示加载条
    even:'true',
    //size: 'lg',
    cols: [[
      {checkbox: true,},
      //{field:'userId', title:'ID',width: 20 },
      {field: 'userAccount', title: '工号', width: 120},
      {field: 'userName', title: '用户名', width: 150},
      {field: 'deptName', title: '所属部门', width: 150},
      {field:'email', title: '邮箱',width: 150},
      {field:'mobile', title: '手机号',width: 120},
      {field: 'isUseable', title: '用户状态', width: 90, templet: '#userStatusTpl'},
      {field: 'operator', title: '操作人', width: 150},
      {field:'createTime', title: '创建时间',width: 150},
      {field: 'updateTime', title: '更新时间', width: 150},
      {fixed:'right', title: '操作', align:'center',width: 195, toolbar: '#userBar'}
    ]],
    page: true,
    limit: 10//默认显示10条
  });

  /**表格重载**/
  window.reload = function () {
    userTableRender.reload({});
  };

  /**监听查询提交*/
  form.on('submit(userSearchFilter)', function (data) {
    if(data.field.sEmail){
      if (!$.checkEmail(data.field.sEmail)){
        layui.layer.msg('邮箱格式不正确', {icon: 5});
        return;
      }
    }
    if(data.field.sMobile){
      if (!$.checkPhone(data.field.sMobile)){
        layui.layer.msg('手机号格式不正确', {icon: 5});
        return;
      }
    }
    userTableRender.reload({
      where: {
        'userAccount': data.field.sUserAccount,
        'userName': data.field.sUserName,
        'email': data.field.sEmail,
        'mobile': data.field.sMobile,
        'isUseable': data.field.isUseable
      }
      });
  });

  /**新增用户*/
  $("#userAdd").click(function () {
		var index = layui.layer.open({
			title : "添加会员",
			type : 2,
			content : "addUser.html",
			success : function(layero, index){
        var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
        //加载部门树数据
        iframeWin.getDept(0);
				setTimeout(function(){
					layui.layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
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
	})

	//批量删除
  $("#userDelBatch").click(function () {
    var checkStatus = table.checkStatus('userTableId'); //userTableId即为基础参数id对应的值
    var data = checkStatus.data;
    if (data.length == 0) {
      layer.msg("请选择需要删除的用户");
      return;
    }
    var userIds = [];
    $.each(data, function (index, item) {
      userIds.push(item.userId);
    });

    layui.layer.confirm('确定要删除选中的记录？', function (index) {
      $.ajax({
        type: "POST",
        url: baseURL + 'sys/user/delete',
        contentType: "application/json",
        data: JSON.stringify(userIds),
        success: function (r) {
          if (r.code === 0) {
            //top.layer.close(index);
            top.layer.msg("用户删除成功！");
            //刷新父页面
            reload();
            layui.layer.close(index);
          } else {
            layui.layer.close(index);
            layer.msg(r.msg);
          }
        }
      });

    });

    //console.log("选中的数据：：："+JSON.stringify(data));

	});

    //全选
	form.on('checkbox(allChoose)', function(data){
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		child.each(function(index, item){
			item.checked = data.elem.checked;
		});
		form.render('checkbox');
	});

	//通过判断文章是否全部选中来确定全选按钮是否选中
	form.on("checkbox(choose)",function(data){
		var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
		var childChecked = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"]):checked')
		if(childChecked.length == child.length){
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = true;
		}else{
			$(data.elem).parents('table').find('thead input#allChoose').get(0).checked = false;
		}
		form.render('checkbox');
	})

  /**监听工具条*/
  table.on('tool(userTable)', function (obj) {
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值
    var userId = data.userId;

    //修改用户
    if (layEvent === 'userEdit') {

      var index = layui.layer.open({
        title: "用户编辑",
        type: 2,
        content: "addUser.html",
        success: function (layero, cIndex) {
          //隐藏密码项
          $('#pwdHid').addClass('layui-hide');
          var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
          //加载部门树数据
          iframeWin.getDept(data.deptId);

          //数据渲染
          var body = layui.layer.getChildFrame('body', cIndex);//获得子页面的dom元素

          body.find('#password').val('');
          body.find('#userId').val(userId);
          body.find('#userAccount').val(data.userAccount);
          body.find('#userName').val(data.userName);
          body.find('#email').val(data.email);
          body.find('#mobile').val(data.mobile);
          // body.find('#dept').val(data.deptName);
          body.find('#deptId').val(data.deptId);


          if (data.isUseable == 0) {
            $('#isUseable').find("option[value='0']").attr("selected", true);
          } else {
            $('#isUseable').find("option[value='1']").attr("selected", true);
          }

          setTimeout(function () {
            layui.layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
              tips: 3
            });
          }, 500);

        }
      })
      //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
      $(window).resize(function () {
        layui.layer.full(index);
      })
      layui.layer.full(index);

      //删除用户
    } else if (layEvent === 'userDel') {

      var status = data.status;
      if (status == 0) {
        layui.layer.msg("当前用户已被删除，请刷新列表");
        return false;
      }
      //if(userId == currentUserId){
      //  common.cmsLayErrorMsg("当前登陆用户不能被失效");
      //  return false;
      //}

      var param = [userId];
      layui.layer.confirm('确定要删除选中的记录？', function (index) {
        $.ajax({
          type: "POST",
          url: baseURL + 'sys/user/delete',
          contentType: "application/json",
          data: JSON.stringify(param),
          success: function (r) {
            if (r.code === 0) {
              //top.layer.close(index);
              top.layer.msg("用户删除成功！");
              //刷新父页面
              reload();
              layui.layer.close(index);
            } else {
              layui.layer.close(index);
              layer.msg(r.msg);
            }
          }
        });

      });

    }
  });

});//layui结束
