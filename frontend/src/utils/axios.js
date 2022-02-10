/* eslint-disable */ 

import axios from "axios";

const request = axios.create({
  baseURL: "http://localhost:8080",
});

export const userAPI = {
  // 회원가입
  register: (nickname, email, password) => {
    console.log(nickname, email, password)
    return request.post("/users/signup/", {
      nickname,
      email,
      password,
    });
  },

  // 로그인
  login: (email, password) => {
    request.post("/user/login", {
      "email": "ssafy@ssafy.com",
      "password": "your_password"
    }).then(res => {
      console.log(res)
    }).catch(err => {
      console.log(err)
    })
  },
};

export const roomAPI = {
  // header에 authorization이 필요하다
  // 게시글 작성
  post: (formData) => {
    return request.post("/room", formData, {
      headers: {
        Authorization: localStorage.getItem("token"),
        "Content-Type": "multipart/form-data",
      },
    });
  },
  // 게시글 가져오기
  get: (search) => {
    return request.get("/room", {
      params: {
        search: search,
      },
    });
  },
};