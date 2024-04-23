import { create } from "zustand";

export const LoginStore = create((set) => ({
  isLogin: false,
  setIsLogin: (login) => set({ isLogin: login }),
}));

export const MainStore = create((set) => ({
  isBgm: true,
  setIsBgm: (bgm) => set({ isBgm: bgm }),
}));
