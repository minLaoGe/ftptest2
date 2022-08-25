function apiConfig() {
  var baseUrl = '';
  // baseUrl = window.location.origin + '/bms-sys';
  // 兼容ie 2019-12-4
  if (window["context"] == undefined) {
    if (!window.location.origin) {
      window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }
    window["context"] = location.origin + "/V6.0";
  }
  baseUrl = window.location.origin;
  console.log(baseUrl);
  return baseUrl;
}

axios.defaults.timeout = 60000;
axios.defaults.baseURL = apiConfig();

axios.defaults.withCredentials = true;  // 意思是携带cookie 信息，保持session一致
if (!window.Promise) {
  window.Promise = Promise;
}

let loading;// 加载框

// http request 拦截器
axios.interceptors.request.use(config => {
  loading = Loading.service({fullscreen:true,text:'拼命加载中',spinner: 'el-icon-loading',background:'rgba(0, 0, 0, 0.8)'});
  if (config.method === 'post') {
    config.data = qs.stringify(config.data);
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// http response 拦截器
axios.interceptors.response.use(response => {
  loading.close();
  let hideXJ = document.getElementById("hideXJ");
  if (response.data.code !== 200) {
	  		 Message({
	      message: response.data.desc,
	      center: true,
	      type: 'error',
	      duration: 1500,
	      customClass: 'xz-alert-common'
	   	 });
  }
  return response;
}, error => {
  // loading.close();
  return Promise.reject(error);
});


/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

 function get(url, params = {}) {
  let URL = url;
  if (params) {
    for (const key in params) {
      URL += params[key] + "/";
    }
    URL = URL.substr(0, URL.length - 1);
  }

  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url: URL,
      // params:params,
      withCredentials: true
    }).then(response => {
      resolve(response.data);
    }).catch(err => {
      reject(err);
    });
  })
}


 function Fn(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url: url,
      params: params,
      withCredentials: true
    }).then(response => {
      resolve(response.data);
    }).catch(err => {
      reject(err);
    })
  })
}

/**
 * 封装get请求
 * 参数放在body内
 * @param url
 * @param params
 * @constructor
 */
 function POSTbyJSON(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: url,
      data: data,
      withCredentials: true,
      headers: {
        'Content-Type': 'application/json;'
      },
      transformRequest: [function (fData, headers) {
        headers['Content-Type'] = 'application/json'
        return JSON.stringify(qs.parse(fData));
      }]
    }).then(function (response) {
      resolve(response.data)
    }).catch(err => {
      reject(err);
    })
  })
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

 function post(url, data) {
  // let d = qs.parse(data);
  // console.log(d);
  // let params = new URLSearchParams();
  // for(let key in data){
  //     if(Array.isArray(data[key])){
  //         for(let val in data[key]){
  //             params.append(key,data[key][val])
  //         }
  //     }else{
  //         params.append(key,data[key]);
  //     }
  // }
  // let params = qs.stringify(data,{ allowDots: true });
  // params.append("roleIds",'2');

  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: url,
      data: data,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      },
      withCredentials: true,
      paramsSerializer: params => {
        return qs.stringify(params, {arrayFormat: 'repeat'});
      }
    }).then(response => {
      resolve(response.data);
    }, err => {
      reject(err)
    })
  })
}

//post请求
 function requestPost(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, params).then(res => {
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
};

//post请求
 function requestPostForm(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    }).then(res => {
      resolve(res.data)//注意res是axios封装的对象，res.data才是服务端返回的信息
    }).catch(error => {
      reject(error)
    })
  })
};

/**
 * 封装delete请求
 * url 地址
 * data 参数  一般是数组
 */
 function Delete(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, {
      params: data,
      withCredentials: true,
      paramsSerializer: params => {
        return qs.stringify(params, {indices: false})
      }
    }).then(response => {
      resolve(response.data);
    }, err => {
      reject(err);
    })
  })
};


/**
 * 封装post请求
 * 审核
 * url 地址
 * data 参数  一般是数组
 */
 function POSTByAudit(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, {
      params: params,
      withCredentials: true,
      paramsSerializer: params => {
        return qs.stringify(params, {indices: false})
      }
    }).then(response => {
      resolve(response.data);
    }, err => {
      reject(err);
    })
  })
};

/**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

 function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}
