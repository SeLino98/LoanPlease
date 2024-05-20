import { create } from 'zustand';
import addSlot from "../Shop/Assets/addSlot.png";

const useStore = create((set) => ({
  // 컴포넌트 토글
  currentComponent: 'gameItem',
  setCurrentComponent: (component) => set({ currentComponent: component }),

  // 유저 정보
  userInfo: null,
  setUserInfo: (item) => set({ userInfo: item }),

  // 유저 포인트(임시)
  point: 0,
  setPoint: (num) => set({ point: num }),

  // 유저 슬롯 개수(임시)
  slotNumber: 0,
  setSlotNumber: (num) => set({ slotNumber: num }), // 구매하면 슬롯 수 변경

  // 슬롯
  slots: [
    {id: 1, name: '슬롯 추가1', icon: addSlot, description: '대출 상품 슬롯을 영구적으로 1칸 추가합니다.', price: '2000', purchased: 0},
    {id: 2, name: '슬롯 추가2', icon: addSlot, description: '대출 상품 슬롯을 영구적으로 1칸 추가합니다.', price: '5000', purchased: 0},
  ],

  // 게임 아이템(임시)
  gameItems: [
    // {id:1, name: '시간 추가', icon: timeExtension, description: '게임 시간을 1분 추가합니다(한 게임 당 1회 한정).', price: '1000', purchased: 0},
    // {id:2, name: 'VIP실 보내기', icon: vip, description: '상품 선택과 상관 없이 최고 점수를 받습니다(1회 한정).', price: '500', purchased: 0},
    // {id:3, name: '보호막', icon: shield, description: '미리 사용 시, 감점 당할 일이 있으면 1회에 한해 방어합니다.', price: '500', purchased: 0},
  ],
  setGameItems: (item) => set({ gameItems: item }),

  // 구매 아이템 선택
  selectedItem: null,
  setSelectedItem: (item) => set({ selectedItem: item }),

  nextSlot: () => set((state) => {
    const nextSlotIndex = state.gameItems.findIndex(item => item.name === '슬롯 추가1' && item.purchased === 1);
    if (nextSlotIndex !== -1) {
      const newItems = [...state.gameItems];
      // 슬롯 추가 1 제거
      newItems.splice(nextSlotIndex, 1);
      // 슬롯 추가 1의 인덱스가 제거된 후에는 슬롯 추가 2를 추가할 때 인덱스를 조정해야 함
      const nextSlotItem = {name: '슬롯 추가2', icon: addSlot, description: '영구적으로 슬롯을 1칸 추가합니다.', price: '5000', purchased: 0};
      // 슬롯 추가 2를 슬롯 추가 1 다음에 추가
      newItems.splice(nextSlotIndex, 0, nextSlotItem);
      return { gameItems: newItems };
    }
    return state;
  }),

  // 대출 아이템(임시)
  loanItems: [
    // {id:1 ,name:'A대출', description: '금리가 어쩌고 설명', price: '123', purchased: 0},
  ],
  setLoanItems: (item) => set({ loanItems: item }),

  // 게임 아이템 개수 설정 모달
  isSetNumberModalOpen: false,
  openSetNumberModal: () => set({ isSetNumberModalOpen: true }),
  closeSetNumberModal: () => set({ isSetNumberModalOpen: false }),

  // 게임 아이템 개수
  itemCount: 1, // 기본 1
  setItemCount: (num) => set({ itemCount: num }),

  // 게임 아이템 구매 확인 모달
  isGameItemModalOpen: false,
  openGameItemModal: () => set({ isGameItemModalOpen: true }),
  closeGameItemModal: () => set({ isGameItemModalOpen: false, isPurchased: false }),

  // 대출 아이템 구매 확인 모달
  isLoanItemModalOpen: false,
  openLoanItemModal: () => set({ isLoanItemModalOpen: true }),
  closeLoanItemModal: () => set({ isLoanItemModalOpen: false, isPurchased: false }),

  // 아이템 상세 모달
  isItemModalOpen: false,
  openItemModal: () => set({ isItemModalOpen: true }),
  closeItemModal: () => set({ isItemModalOpen: false }),

  // 포인트 부족 시 경고 모달
  isWarningModalOpen: false,
  openWarningModal: () => set({ isWarningModalOpen: true }),
  closeWarningModal: () => set({ isWarningModalOpen: false }),

  // 슬롯 저장 모달
  isSaveSlotModalOpen: false,
  openSaveSlotModal: () => set({ isSaveSlotModalOpen: true }),
  closeSaveSlotModal: () => set({ isSaveSlotModalOpen: false }),

  // 슬롯 저장 경고 모달
  isWarningModal2Open: false,
  openWarningModal2: () => set({ isWarningModal2Open: true }),
  closeWarningModal2: () => set({ isWarningModal2Open: false }),

  // 구매확정 모달로 전환
  isPurchasing: false,
  setIsPurchasing: () => set({ isPurchasing: true }),

  // 화면에 보이는 슬롯 채우기
  selected1: {name: null},
  selected2: {name: null},
  selected3: {name: null},
  selected4: {name: null},
  selected5: {name: null},
  setSelected1: (item) => set({ selected1: {name: item.name} }),
  setSelected2: (item) => set({ selected2: {name: item.name} }),
  setSelected3: (item) => set({ selected3: {name: item.name} }),
  setSelected4: (item) => set({ selected4: {name: item.name} }),
  setSelected5: (item) => set({ selected5: {name: item.name} }),

  // 유저가 가지고 있는 아이템
  products: [
    // {name: '상품1', content: '설명1', ...},
  ],
  setProducts: (item) => set({ products: item }),

  purchasedProducts: [],
  setPurchasedProducts: (prod) => set({ purchasedProducts: prod }),

  // 모달에 뜰 설명
  selectedProduct: '',
  setSelectedProduct: (description) => set({ selectedProduct: description }),

  // 세팅된 슬롯:
  savedSlot: [0, 0, 0, 0, 0],
  setSavedSlot: (item) => set({ savedSlot: item }),
  
  // 슬롯용 페이지네이션
  currentPage: 0,
  setCurrentPage: (page) => set({ currentPage: page }),

  // 대출 아이템용 페이지네이션
  currentPage2: 0,
  setCurrentPage2: (page) => set({ currentPage2: page }),
}));

export default useStore;