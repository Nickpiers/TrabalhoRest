import { useState } from "react";
import { criarReserva } from "../../../controller/requestRest";
import { Cabecalho } from "../../Dumb/Cabecalho";
import { Fundo } from "../../Dumb/Fundo";
import { Input } from "../../Dumb/Input";
import { CruzeiroCard } from "../../Dumb/CruzeiroCard";
import { useLocation, useNavigate } from "react-router-dom";

export const MarcarReserva = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { cruzeiro } = location.state;

  const [nomeCompleto, setNomeCompleto] = useState("");
  const [dataEmbarque, setDataEmbarque] = useState("");
  const [numeroPassageiros, setNumeroPassageiros] = useState("");
  const [numeroCabines, setNumeroCabines] = useState("");

  const marcarReserva = async () => {
    await criarReserva(
      {
        nomeCompleto,
        dataEmbarque,
        numeroPassageiros,
        numeroCabines,
        idCruzeiro: cruzeiro.idCruzeiro,
      },
      navigate
    );
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Criar Reserva" />
        <div className="flex flex-col gap-5">
          <CruzeiroCard cruzeiro={cruzeiro} />
          <Input
            value={nomeCompleto}
            setValue={setNomeCompleto}
            descricao="Nome Completo"
          />
          <Input
            value={dataEmbarque}
            setValue={setDataEmbarque}
            descricao="Data de Embarque"
          />
          <Input
            value={numeroPassageiros}
            setValue={setNumeroPassageiros}
            descricao="Número de Passageiros"
          />
          <Input
            value={numeroCabines}
            setValue={setNumeroCabines}
            descricao="Número de Cabines"
          />
        </div>
        <button className="mb-30 mt-5" onClick={marcarReserva}>
          Reservar
        </button>
      </div>
      <Fundo />
    </div>
  );
};
