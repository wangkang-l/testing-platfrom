<html>
<body>
<h3>Hi All， 本次自动化测试结果如下：</h3>
<table border="1">
    <tr>
        <td>任务名称</td>
        <td>批次号</td>
        <td>开始时间</td>
        <td>结束时间</td>
        <td>用例数</td>
        <td>通过数</td>
        <td>失败数</td>
        <td>通过率</td>
        <td>详情</td>
    </tr>
    <tr>
        <td>${task_name}</td>
        <td>${batch_no}</td>
        <td>${start_time?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td>${end_time?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td>${case_num}</td>
        <td>${pass_num}</td>
        <td>${fail_num}</td>
        <td>${pass_rate}</td>
        <td>< a href=" " target="_blank">详情</ a></td>
    </tr>
</table>
</body>
</html>