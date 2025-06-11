export const Input = ({ value, setValue, descricao }) => {
  return (
    <div className="flex items-center gap-4 mb-4">
      <label className="w-40 text-center text-white">{`${descricao}:`}</label>
      <input
        type="text"
        className="flex-1 bg-white border border-gray-300 text-black px-3 py-2 rounded"
        value={value}
        onChange={(e) => setValue(e.target.value)}
      />
    </div>
  );
};
