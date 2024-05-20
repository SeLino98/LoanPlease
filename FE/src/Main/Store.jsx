import { create } from "zustand";

export const LoginStore = create((set) => ({
  isLogin: false,
  setIsLogin: (login) => set({ isLogin: login }),
  mydata: {
    image: "/loanplease.png",
    nick: "-",
    email: "",
    rank: "-",
  },
  setMyData: (data) => set({ mydata: data }),
}));

export const MainStore = create((set) => ({
  isBgm: true,
  setIsBgm: (bgm) => set({ isBgm: bgm }),
  rankingpopup: false,
  setRankingpopup: (value) => set({ rankingpopup: value }),
  mypagepopup: false,
  setMypagepopup: (value) => set({ mypagepopup: value }),
  dialogs: [
    "당신에게 딱 맞는 대출 상품을 찾아드립니다!",
    "잠깐! 대출에 필요한 서류는 모두 준비되었나요?",
    "혹시 당신도 우대고객이신가요?",
    "신용점수 올리는 방법이 궁금한가요?",
    "어서오세요 고객님, 무슨 일로 찾아오셨나요?",
    "신용점수가 높을수록 더 많은 상품을 추천해드릴 수 있답니다!",
    "찾아주셔서 감사합니다. 오늘도 좋은 하루 되세요!",
    "신용점수는 신용평가사에서 제공하는 금융정보에요",
    "게임 시작 전에 추천할 상품은 신중하게 정해주세요",
    "어떤 상품을 추천해야 할지 잘 모르겠다고요? 아이템의 도움을 받아보는 건 어떤가요?",
    "대출은 하는 것보다 제때 갚는 것이 더 중요해요. 신용등급에 매우 큰 영향을 주기 때문이죠.",
    "통신비와 공공요금은 제때 납부해주세요",
    "같은 신용등급이라도 조건에 따라 대출 승인 여부가 달라질 수 있어요",
    "당신의 신용점수는 얼마인가요? 마이페이지에서 확인해보세요",
    "신용카드는 상환 능력만 받쳐준다면 적절히 이용하는 것이 신용점수에 이로워요",
    "신용점수가 낮으면 신용카드 발급이 안 될 수도 있어요!",
  ],
}));

export const RankingStore = create((set) => ({
  index: 0,
  setIndex: (value) => set({ index: value }),
  searchmode: false,
  setSearchmode: (value) => set({ searchmode: value }),
  inputdata: '',
  setInputdata: (value) => set({ inputdata: value }),
}));

export const MypageStore = create((set) => ({
  editmode: false,
  setEditmode: (value) => set({ editmode: value }),
}));
