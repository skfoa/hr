// 全局 JavaScript 文件
console.log("HR Management System Global Script Loaded.");

// 示例：可以添加一些通用的DOM操作或事件监听器
document.addEventListener('DOMContentLoaded', function() {
    // 例如：给所有class为 'confirm-delete' 的链接添加点击确认
    const deleteLinks = document.querySelectorAll('a.confirm-delete'); // 假设删除链接有此class
    deleteLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            const message = this.getAttribute('data-confirm-message') || '确定要执行此操作吗？';
            if (!confirm(message)) {
                event.preventDefault(); // 阻止默认行为 (跳转)
            }
        });
    });

    // 激活Bootstrap表单验证 (如果 department-form.html 中的脚本移到这里)
    // (function () {
    //     'use strict'
    //     var forms = document.querySelectorAll('.needs-validation')
    //     Array.prototype.slice.call(forms)
    //         .forEach(function (form) {
    //             form.addEventListener('submit', function (event) {
    //                 if (!form.checkValidity()) {
    //                     event.preventDefault()
    //                     event.stopPropagation()
    //                 }
    //                 form.classList.add('was-validated')
    //             }, false)
    //         })
    // })()
});

// 可以在这里添加更多的全局函数或特定页面的初始化逻辑（通过判断页面标识）