import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { App } from "./App";
import { PromocaoSSEProvider } from "./components/PromocaoSSEContext";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <PromocaoSSEProvider>
      <App />
    </PromocaoSSEProvider>
  </StrictMode>
);
