layui.config({
	base : "../../js/"
}).use(['table','form','layer'],function(){
    var table = layui.table,
        form = layui.form,
        $ = layui.jquery;

    /**配置表格加载*/
    var configTableRender = table.render({
      elem: '#configTableList',
      url: baseURL+'sys/config/list',
      id:'configTableId',
      method: 'post',
      height:'480',
      skin:'row',
      even:'true',
      //size: 'lg',
      loading: 'true',//是否显示加载条
      cols: [[
        {checkbox: true,},
        {field:'key', title: '参数名',width: 300},
        {field:'value', title: '参数值',width: 700},
        {field:'remark', title: '备注',width: 300},
      ]],
      page: true,
      limit: 10//默认显示10条
    });

    /**查询*/
    $("#configSearch").click(function(){
      //监听提交
      form.on('submit(configSearchFilter)', function (data) {
        configTableRender.reload({
          where: {
            searchTerm:data.field.searchTerm,
            searchContent:data.field.searchContent
          }
        });
      });

    });

})