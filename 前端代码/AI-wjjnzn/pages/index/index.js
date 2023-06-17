// index.js
Page({
  data:{

  },
  //图片单击事件
  onAvatarTap:function(){
    //判断用户是否登录
    const appInstance = getApp()
    const isLogin = appInstance.globalData.isLogin
    if(isLogin){
      wx.navigateTo({
        url: '/pages/chat/chat',
      })
    }else{
      wx.switchTab({
        url: '/pages/login/login',
      })
    }
  }
})
