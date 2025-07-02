import { createContext, useContext, useRef } from "react";
import { escutarPromocao } from "../controller/requestRest";

const PromocaoSSEContext = createContext();

export const PromocaoSSEProvider = ({ children }) => {
  const eventSources = useRef({});

  const startSSE = (clientId, idPromocao) => {
    const chave = `${idPromocao}`;
    if (eventSources.current[chave]) return;

    const es = escutarPromocao(clientId, idPromocao);
    eventSources.current[chave] = es;
  };

  const stopSSE = (idPromocao) => {
    const chave = `${idPromocao}`;
    const es = eventSources.current[chave];
    if (es) {
      es.close();
      delete eventSources.current[chave];
    }
  };

  return (
    <PromocaoSSEContext.Provider value={{ startSSE, stopSSE }}>
      {children}
    </PromocaoSSEContext.Provider>
  );
};

export const usePromocaoSSE = () => useContext(PromocaoSSEContext);
