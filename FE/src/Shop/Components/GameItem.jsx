import addSlot from '../Assets/addSlot.png';
import timeExtension from '../Assets/timeExtension.png';

function GameItem({ openModal }) {
  return (
    <div className="flex justify-center gap-4 my-2 text-center">
      <div className="border-2 bg-white px-6 py-4 rounded-lg">
        <img src={addSlot} alt="슬롯추가" className="w-32 h-32 my-3" />
        <p className="font-cusFont1 my-2">슬롯 추가</p>
        <p className="font-cusFont2 my-2">설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
      <div className="border-2 bg-white px-6 py-4 rounded-lg">
        <img src={timeExtension} alt="시간연장" className="w-32 h-32 my-3" />
        <p className="font-cusFont1 my-2">시간 연장</p>
        <p className="font-cusFont2 my-2">설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
      <div className="border-2 bg-white px-6 py-4 rounded-lg">
        <img src={timeExtension} alt="" className="w-32 h-32 my-3" />
        <p className="font-cusFont1 my-2">아이템</p>
        <p className="font-cusFont2 my-2">설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
    </div>
  )
}

export default GameItem;