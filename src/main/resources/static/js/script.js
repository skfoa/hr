// 全局 JavaScript 文件
console.log("HR Management System Global Script Loaded.");

// 可以添加一些通用的DOM操作或事件监听器
document.addEventListener('DOMContentLoaded', function() {
    // 给所有class为 'confirm-delete' 的链接添加点击确认
    const deleteLinks = document.querySelectorAll('a.confirm-delete');
    deleteLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            const message = this.getAttribute('data-confirm-message') || '确定要执行此操作吗？';
            if (!confirm(message)) {
                event.preventDefault(); // 阻止默认行为 (跳转)
            }
        });
    });
});

