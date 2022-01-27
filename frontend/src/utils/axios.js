import axios from "axios";

const request = axios.create({
  baseURL: "http://localhost:8888/api",
});

export const userAPI = {
  // 회원가입
  register: (name, email, password) => {
    return request.post("/user", {
      name,
      email,
      password,
    });
  },

  // 로그인
  login: (email, password) => {
    return request.post("/user/login", {
      email,
      password,
    });
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
