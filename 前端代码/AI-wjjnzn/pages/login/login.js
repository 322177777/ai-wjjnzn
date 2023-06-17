// pages/login/login.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    nickname: '',
    avatarUrl: '',
    openId: '',
    isLogin: false,
    hidden: false
  },
  //单击登录
  onLogin: function (e) {
    wx.showLoading({
      title: '登录中...',
    });
    setTimeout(function () {
      wx.hideLoading()
    }, 1000);
    var that = this;
    wx.login({
      success(res) {
        if (res.code) {
          console.log('登录成功，code：', res.code)
          // 获取用户信息
          wx.getUserInfo({
            success(infoRes) {
              console.log('获取用户信息：', infoRes.userInfo)
              that.data.nickname = infoRes.userInfo.nickName;
              that.data.avatarUrl = infoRes.userInfo.avatarUrl;
              console.log(infoRes.userInfo.nickName)
            }
          })
          setTimeout(function () {
            // 发送 res.code 到后台换取 openId, sessionKey, unionId 等信息
            // 可通过 wx.request 将 code 发送至服务器端
            wx.request({
              url: 'http://127.0.0.1:8080/login',
              method:'GET',
              data:{
                code: res.code,
                nickname: that.data.nickname,
                avatarUrl: that.data.avatarUrl
              },
              success:function(res){
                //处理返回
                var result = res.data;
                console.log(result)
                if (result.code == '1') {
                  that.setData({
                    isLogin: true,   // 显示账号信息组件
                    hidden: true,
                    avatarUrl: that.data.avatarUrl,
                    nickname: that.data.nickname,
                    openId: result.retData.openId
                  });
                // 获取字段的值
                //var token = result.reData.token;
                //that.data.openId = result.retData.openId;
                //验证信息
                //...

                //更改用户权限
                let appInstance = getApp();
                appInstance.loginSuccess();
                } else {
                  // 登录失败，弹出提示框等处理
                  wx.showToast({
                    title: '登录失败，请重试',
                    icon: 'none',
                  });
                }
              },
              fail:function(res){
                console.log('请求失败')
              }
            })
          },1000);
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    })
  }
})