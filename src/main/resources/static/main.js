// 获取表单对象
const form = document.getElementById('form-id');

// 获取提交按钮
const submitButton = document.getElementById('sub-btn');

// 为提交按钮添加事件处理程序
submitButton.addEventListener('click', function (event) {
    // 获取所有 img 元素
    const imgs = document.querySelectorAll('img');

    // 遍历 img 元素
    for (let i = 0; i < imgs.length; i++) {
        // 获取当前 img 元素
        const img = imgs[i];

        // 获取 img 元素的父元素
        const parent = img.parentNode;

        // 从父元素中删除 img 元素
        parent.removeChild(img);
    }

    // 阻止默认事件（提交表单）
    event.preventDefault();

    // 创建一个 FormData 对象
    const formData = new FormData(form);

    // 创建一个 XMLHttpRequest 对象
    const xhr = new XMLHttpRequest();

    // 设置请求的 URL
    xhr.open('POST', 'http://localhost:8080/get-image');

    // 设置请求的回调函数
    xhr.onload = function () {
        // 解析响应数据
        const response = JSON.parse(xhr.responseText);

        // 获取 urlList 数组
        const urlList = response.urlResponse.urlList;

        // 遍历 urlList 数组
        for (let i = 0; i < urlList.length; i++) {
            // 创建一个 img 标签
            const img = document.createElement('img');
            img.style.textAlign = 'center';

            // 设置 img 标签的 src 属性
            img.src = urlList[i];

            // 将 img 标签添加到文档中
            document.body.appendChild(img);
        }
    };

    // 发送请求
    xhr.send(formData);
});
