import { useEffect, useState } from "react";
import fundo from "../public/fundoTela.jpg";
import {
  cancelarPromocao,
  cancelarReserva,
  consultarItinerario,
  criarReserva,
  inscreverPromocao,
  requestBack,
} from "./controller/requestRest";

export const Home = () => {
  const [mensagem, setMensagem] = useState("");

  useEffect(() => {
    const testeConexaoBack = async () => {
      const response = await requestBack("/reservas/testeConexao");
      setMensagem(response);
    };
    testeConexaoBack();
  }, []);

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <h1 className="absolute top-4 left-0 w-full text-center text-2xl font-bold">
          Reserva de Cruzeiros
        </h1>
        <div className="flex flex-col gap-5 mt-20">
          <button onClick={consultarItinerario}>Consultar Itinerários</button>
          <button onClick={criarReserva}>Efetuar Reserva</button>
          <button onClick={cancelarReserva}>Cancelar Reserva</button>
          <button onClick={inscreverPromocao}>Inscreva-se na promoção!</button>
          <button onClick={cancelarPromocao}>Cancelar promoção</button>
        </div>
        <p className="mt-10 text-sm text-gray-600">{mensagem}</p>
      </div>
      <div
        className="w-1/2 bg-cover bg-center"
        style={{ backgroundImage: `url(${fundo})` }}
      />
    </div>
  );
};
