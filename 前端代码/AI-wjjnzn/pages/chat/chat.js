const apiUrl = 'ws://127.0.0.1:8080/chatai';

Page({
  data: {
    chatHistory: []
  },

  onLoad() {
    this.socketTask = wx.connectSocket({
      url: apiUrl,
      success: () => console.log('WebSocket 连接成功')
    });

    // 处理 WebSocket 连接事件
    this.socketTask.onOpen(() => {
      console.log('WebSocket 连接已打开');
    });

    this.socketTask.onMessage((res) => {
      console.log(`收到服务器消息：${res.data}`);
      const aiMessage = {
        content: res.data,
        isAI: true
      };
      this.setData({
        chatHistory: [...this.data.chatHistory, aiMessage]
      });
    });

    this.socketTask.onClose(() => {
      console.log('WebSocket 连接已关闭');
    });

    this.socketTask.onError((err) => {
      console.error('WebSocket 连接发生错误：', err);
    });
  },

  onInput(event) {
    this.content = event.detail.value;
  },

  onSubmit:function() {
    if (!this.content) return;

    const userMessage = { content: this.content, isAI: false };
    this.setData({
      chatHistory: [...this.data.chatHistory, userMessage]
    });
    const sendData = {
      userId:'o4Qut4mOQmrYqYsFgHVNZ6E5mtUs',
      content: this.content
    };
    this.socketTask.send({
      data: JSON.stringify(sendData),
      success: () => console.log(`发送消息：${this.content}`)
    });

    this.content = '';
  },

  onUnload() {
    if (this.socketTask) {
      this.socketTask.close({
        success: () => console.log('WebSocket 连接已断开')
      });
    }
  }
});
