import fundo from "../../../public/fundoTela.jpg";

export const Fundo = () => {
  return (
    <div
      className="w-1/2 bg-cover bg-center"
      style={{ backgroundImage: `url(${fundo})` }}
    />
  );
};
