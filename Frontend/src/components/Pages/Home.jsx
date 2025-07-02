import { v4 as uuidv4 } from "uuid";

import { useEffect } from "react";
import { requestBack } from "../../controller/requestRest";
import { paths } from "../../controller/paths";
import { useNavigate } from "react-router-dom";
import { Fundo } from "../Dumb/Fundo";

export const Home = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const testeConexaoBack = async () => {
      await requestBack("/reservas/testeConexao");
    };
    const existingId = sessionStorage.getItem("clientId");
    if (!existingId) {
      const newId = uuidv4();
      sessionStorage.setItem("clientId", newId);
    }
    testeConexaoBack();
  }, []);

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <h1 className="absolute top-4 left-0 w-full text-center text-2xl font-bold">
          Reserva de Cruzeiros
        </h1>
        <div className="flex flex-col gap-5 mt-20">
          <button onClick={() => navigate(paths.consultarItinerarios)}>
            Consultar Itinerários
          </button>
          <button onClick={() => navigate(paths.reservas)}>Reservas</button>
          <button onClick={() => navigate(paths.promocoes)}>Promoções</button>
        </div>
      </div>
      <Fundo />
    </div>
  );
};
