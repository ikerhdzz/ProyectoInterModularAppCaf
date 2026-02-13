import { createContext, useContext, useState } from "react";

const CarritoContext = createContext<any>(null);

export const CarritoProvider = ({ children }: { children: React.ReactNode }) => {
  const [carrito, setCarrito] = useState<any[]>([]);

  const agregar = (producto: any) => {
    setCarrito([...carrito, producto]);
  };

  const limpiar = () => setCarrito([]);

  return (
    <CarritoContext.Provider value={{ carrito, agregar, limpiar }}>
      {children}
    </CarritoContext.Provider>
  );
};

export const useCarrito = () => useContext(CarritoContext);
