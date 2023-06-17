// app.js
App({
  globalData: {
    isLogin: false, // 用户是否登录
  },
  onLaunch: function () {
    // ...
  },
  loginSuccess: function () {
    this.globalData.isLogin = true;
  }
})
