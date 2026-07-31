import { cn } from "src/shared/lib/utils";
import { ChevronDown } from "lucide-react";
import { useState, useRef, useEffect } from "react";

type SelectOption = {
  value: string;
  label: string;
};

type SelectProps = {
  value: string;
  onChange: (value: string) => void;
  options: SelectOption[];
  className?: string;
};

export function Select({ value, onChange, options, className }: SelectProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedLabel, setSelectedLabel] = useState(
    options.find((opt) => opt.value === value)?.label || value,
  );
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    setSelectedLabel(
      options.find((opt) => opt.value === value)?.label || value,
    );
  }, [value, options]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleSelect = (optionValue: string) => {
    onChange(optionValue);
    setIsOpen(false);
  };

  return (
    <div ref={containerRef} className="relative">
      <button
        type="button"
        onClick={() => setIsOpen(!isOpen)}
        className={cn(
          "h-9 min-w-[100px] rounded-xl border-0 bg-muted/50 px-4 pr-9 text-sm font-medium text-foreground",
          "transition-all duration-200",
          "hover:bg-muted hover:shadow-sm",
          "focus:outline-none focus:ring-2 focus:ring-primary/30 focus:bg-muted",
          "cursor-pointer flex items-center justify-between",
          className,
        )}
      >
        <span>{selectedLabel}</span>
        <ChevronDown
          className={cn(
            "h-4 w-4 text-muted-foreground transition-transform duration-200",
            isOpen && "rotate-180",
          )}
        />
      </button>

      {isOpen && (
        <div className="absolute top-full left-0 right-0 mt-2 z-50 rounded-xl border border-border/50 bg-popover shadow-lg overflow-hidden animate-in fade-in-0 zoom-in-95 duration-200">
          <div className="py-1">
            {options.map((opt) => (
              <button
                key={opt.value}
                type="button"
                onClick={() => handleSelect(opt.value)}
                className={cn(
                  "w-full px-4 py-2.5 text-sm font-medium text-left transition-colors duration-150",
                  "hover:bg-accent hover:text-accent-foreground",
                  "focus:bg-accent focus:text-accent-foreground focus:outline-none",
                  value === opt.value && "bg-primary/10 text-primary",
                )}
              >
                {opt.label}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
