import type { FC } from "react";

export const Button: FC<{ msg: string; handleClick: () => void }> = ({
  msg,
  handleClick,
}) => {
  return (
    <button onClick={handleClick} type="button">
      {msg}
    </button>
  );
};
