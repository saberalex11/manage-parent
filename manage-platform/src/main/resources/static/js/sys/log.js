layui.config({
	base : "/js/"
}).use(['table','form','layer'],function(){
    var table = layui.table,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    /**日志表格加载*/
    var logTableRender = table.render({
      elem: '#logTableList',
      url: baseURL +'sys/log/list',
      id:'logTableId',
      method: 'post',
      height:'480',
      skin:'row',
      even:'true',
      loading: 'true',//是否显示加载条
      //size: 'lg',
      cols: [[
        {checkbox: true,fixed:'left',},
        {field:'userName', title: '用户名',width: 120 },
        {field:'operation', title: '用户操作',width: 90},
        {field:'method', title: '请求方法',width: 150},
        {field:'params', title: '请求参数',width: 150},
        {field:'time', title: '执行时长（毫秒）',width: 120},
        {field:'ip', title: 'ip地址',width: 120},
        {field:'createTime', title: '创建时间',width: 150}
      ]],
      page: true,
      limit: 10//默认显示10条
    });

    /**查询*/
    $("#logSearch").click(function(){
      //监听提交
      form.on('submit(logSearchFilter)', function (data) {
        logTableRender.reload({
          where: {
            key:data.field.searchTerm,
            key:data.field.searchContent
          }
        });
      });

    });

})