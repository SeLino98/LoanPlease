import { useRef } from "react";
import { purchaseGameItem } from "../API/ShopAPI";
import { PropTypes } from "prop-types"; 
import useStore from "../../Store/ShopStore";

function SetNumberModal({ closeSetNumberModal, openGameItemModal, openWarningModal, point, itemId, price }) {
  const { itemCount, setItemCount, setPoint } = useStore();
  const modalRef = useRef();

  // 모달 바깥을 클릭하면 모달이 닫히도록
  const handleOutsideClick = (e) => {
    if (modalRef.current && !modalRef.current.contains(e.target)) {
      closeSetNumberModal();
      setItemCount(1);
    }
  };

  const decrement = () => {
    if (itemCount > 0) {
      setItemCount(itemCount - 1);
    }
  }

  const increment = () => {
    setItemCount(itemCount + 1);
  }

  const handlePurchaseItems = async () => {
    if (point < price * itemCount) {
      openWarningModal();
      return;
    } else {
      try {
        const data = await purchaseGameItem(itemId, itemCount);

        closeSetNumberModal(); 
        // 구매 후 포인트 업데이트
        setPoint(data.remainPoint);

        setItemCount(1);  // 구매 후 모달 닫으면 1로 초기화
        
        openGameItemModal(); 
      } catch (error) {
        console.error(error)
      }
    }
  }

  return(
    <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-800 bg-opacity-50 z-20" onClick={handleOutsideClick}>
      <style>
        {/* jsx에서 html 태그 안에 스타일 적용 시 {} 필요 */}
        {/* Chrome, Safari, Edge, Opera */}
        {`
          input::-webkit-outer-spin-button,
          input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
          }
        `} 

        {/* Firefox */}
        {`
          input[type=number] {
            -moz-appearance: textfield;
          }
        `}
      </style>
      <div ref={modalRef} className="w-[270px] h-[180px] bg-yellow-200 p-4 rounded-md font-cusFont1 text-center border-2 z-50 border-black flex flex-col justify-center">
        <div>
          <div className="h-[45px] text-xl place-content-center">
            <p>개수설정</p>
          </div>
          {/* 여기에 카운터 들어감 */}
          <div className="flex flex-row h-10 w-full rounded-lg relative bg-transparent mt-1">
            {/* - */}
            <button 
              className="bg-gray-300 text-gray-600 hover:text-gray-700 hover:bg-gray-400 h-full w-20 rounded-l cursor-pointer outline-none focus:outline-none"
              onClick={decrement}
            >
              <span className="m-auto text-2xl font-thin">-</span>
            </button>
            {/* 숫자 표시 */}
            <input 
              type="number" 
              className="outline-none focus:outline-none text-center w-full bg-gray-300 font-semibold text-md hover:text-black focus:text-black md:text-basecursor-default flex items-center text-gray-700 outline-none focus:outline-none" 
              value={itemCount}
              onChange={(e) => setItemCount(parseInt(e.target.value))}
            ></input>
            {/* + */}
            <button 
              className="bg-gray-300 text-gray-600 hover:text-gray-700 hover:bg-gray-400 h-full w-20 rounded-r cursor-pointer focus:outline-none"
              onClick={increment}
            >
              <span className="m-auto text-2xl font-thin">+</span>
            </button>
          </div>
          <div className="flex justify-center mt-4">
            <button 
              className="mx-2 px-4 py-2 bg-blue-300 hover:bg-blue-500 rounded-md border-2 border-b-4 border-black focus:ring-4 shadow-lg transform active:scale-y-75 transition-transform" 
              onClick={() => { 
                // 구매 함수 실행
                handlePurchaseItems();
              }}
              disabled={itemCount === 0}
            >
              확인
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}



SetNumberModal.propTypes = {
  closeSetNumberModal: PropTypes.func.isRequired,
  openGameItemModal: PropTypes.func.isRequired,
  openWarningModal: PropTypes.func.isRequired,
  itemId: PropTypes.number.isRequired,
  price: PropTypes.number.isRequired,
  point: PropTypes.number.isRequired,
};

export default SetNumberModal;