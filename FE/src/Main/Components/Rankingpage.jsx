import { useEffect, useState } from "react";
import { RankingStore } from "../Store";
import search from "./assets/search_icon.png";
import Rankingdata from "./Rankingdata";
import SearchPage from "./SearchPage";
import { rankinglist, friendrankinglist } from "../../API/API";

const rankingstyleClass = `
flex justify-center items-center 
border-4 border-black rounded-[30px] 
bg-[#ffc436] px-[30px] py-[20px] min-w-[300px] 
`;

const rankingboardstyleClass = `
z-[1] w-full h-full 
rounded-[30px] px-5 py-2.5 
bg-white
`;

const titleareaClass = `
flex justify-around items-center 
w-full h-[50px] 
font-cusFont1 text-xl 
`;

const buttonareaClass = `
flex justify-around w-full h-10 
`;

const buttonstyleClass = `
flex justify-center items-center 
w-[45%] bg-[#A9A9A9] rounded-[20px] 
text-xs font-cusFont1 cursor-pointer 
`;

const rankingresultareaClass = `
w-full h-[200px] my-5 overflow-y-auto 
`;

// const rankingstyle = {
//   display: "flex",
//   justifyContent: "center",
//   alignItems: "center",
//   border: "4px solid",
//   borderRadius: "30px",
//   backgroundColor: "#FFC436",
//   padding: "20px 30px",
// };

// const rankingboardstyle = {
//   backgroundColor: "#ffffff",
//   zIndex: "1",
//   width: "100%",
//   height: "100%",
//   borderRadius: "30px",
//   padding: "10px 20px",
// };

// const titlearea = {
//   display: "flex",
//   justifyContent: "space-around",
//   alignItems: "center",
//   width: "100%",
//   height: "50px",
//   fontFamily: "비트비트체v2",
//   fontSize: "20px",
// };

// const buttonstyle = {
//   width: "45%",
//   backgroundColor: "#A9A9A9",
//   borderRadius: "20px",
//   display: "flex",
//   justifyContent: "center",
//   alignItems: "center",
//   fontSize: "12px",
//   fontFamily: "비트비트체v2",
//   cursor: "pointer",
// };

// const rankingresultarea = {
//   width: "100%",
//   height: "200px",
//   margin: "20px 0px",
//   overflowY: "auto",
// };

// const rankingdummydata = [
//   {
//     image: "",
//     nickname: "fdvddfv",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "dfghfgdhsgg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "4t3gfre",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
//   {
//     image: "",
//     nickname: "aergsrtg",
//     score: 10000,
//   },
// ];

function Rankingpage() {
  // const selected = { ...buttonstyle, backgroundColor: "#FFD1E3" };
  const selected = buttonstyleClass + " bg-[#FFD1E3]";
  const buttons = [selected, buttonstyleClass];
  const searchmode = RankingStore((state) => state.searchmode);
  const setSearchmode = RankingStore((state) => state.setSearchmode);
  const index = RankingStore((state) => state.index);
  const setIndex = RankingStore((state) => state.setIndex);

  const [datalist, setDataList] = useState(null);

  const getallrankings = async () => {
    rankinglist().then((result) => {
      const resultdata = (
        <div>
          {result.data &&
            result.data.map((value, index) => {
              return (
                <Rankingdata key={index + 1} data={value} rank={index + 1} />
              );
            })}
        </div>
      );
      setDataList(resultdata);
    });
  };

  const getallfriendrankings = async () => {
    friendrankinglist().then((result) => {
      const resultdata = (
        <div>
          {result.data &&
            result.data.map((value, index) => {
              return (
                <Rankingdata key={index + 1} data={value} rank={index + 1} />
              );
            })}
        </div>
      );
      setDataList(resultdata);
    });
  };

  const switchmode = () => {
    setIndex(1 - index);
    setSearchmode(false);
  };

  const makeClickSound = () => {
    const audio = new Audio("audioes/pop_sound_2.mp3");
    audio.play();
  };

  useEffect(() => {
    getallrankings();
  }, []);

  return (
    // <div style={rankingstyle}>
    <div className={rankingstyleClass}>
      {/* <div style={rankingboardstyle}> */}
      <div className={rankingboardstyleClass}>
        {/* <div style={titlearea}> */}
        <div className={titleareaClass}>
          {/* <div style={{ width: "30px", height: "30px" }}></div> */}
          <div className="h-[30px] w-[30px]"></div>
          <div>Ranking</div>
          <img
            className="h-[30px] w-[30px] cursor-pointer"
            onClick={() => {
              setSearchmode(!searchmode);
            }}
            src={search}
          />
        </div>
        <div
          // style={{
          //   display: "flex",
          //   justifyContent: "space-around",
          //   width: "100%",
          //   height: "40px",
          // }}
          className={buttonareaClass}
        >
          <div
            onClick={async () => {
              if (index == 1) {
                makeClickSound();
                switchmode();
                getallrankings();
              }
            }}
            className={buttons[index]}
          >
            전체랭킹
          </div>
          <div
            onClick={async () => {
              if (index == 0) {
                makeClickSound();
                switchmode();
                getallfriendrankings();
              }
            }}
            className={buttons[1 - index]}
          >
            친구랭킹
          </div>
        </div>
        {searchmode ? (
          <SearchPage />
        ) : (
          // <div style={rankingresultarea}>
          <div className={rankingresultareaClass}>{datalist}</div>
        )}
      </div>
    </div>
  );
}

export default Rankingpage;
