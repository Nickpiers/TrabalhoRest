import { useNavigate } from "react-router-dom";

export const Cabecalho = ({ titulo }) => {
  const navigate = useNavigate();

  return (
    <>
      <button
        onClick={() => navigate(-1)}
        className="absolute top-7 left-4 bg-gray-800 text-blue-500 px-4 py-1 rounded z-10"
      >
        Voltar
      </button>
      <h1 className="absolute top-4 left-0 w-full text-center text-2xl font-bold z-0">
        {titulo}
      </h1>
    </>
  );
};
