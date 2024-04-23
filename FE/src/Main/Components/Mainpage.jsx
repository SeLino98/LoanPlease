import Buttonbar from "./Buttonbar";
import Menubar from "./Menubar";
import background from "./assets/splash.png";

const mainstyle = {
  width: "100%",
  height: "100vh",
  display: "flex",
  flexDirection: "column",
  justifyContent: "space-between",
};

const backgroundstyle = {
  zIndex: "-1",
  position: "absolute",
  width: "100%",
  top: "100px",
  height: "calc(100vh - 100px)",
};

function Main() {
  return (
    <div style={mainstyle}>
      <Menubar />
      <img style={backgroundstyle} src={background} />
      <Buttonbar />
    </div>
  );
}

export default Main;
