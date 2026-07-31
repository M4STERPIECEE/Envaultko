import { createFormHook } from "@tanstack/react-form";
import { AllocationField } from "./allocation-field";
import { AmountField } from "./amount-field";
import { AutocompleteField } from "./autocomplete-field";
import { ColorField } from "./color-field";
import { ComboboxField } from "./combobox-field";
import { DatePickerField } from "./date-picker-field";
import {
  fieldContext,
  formContext,
  useFieldContext,
  useFormContext,
} from "./form-context";
import { InputField } from "./input-field";
import { OtpField } from "./otp-field";
import { SubmitButton } from "./submit-button";

export const { useAppForm, withForm, withFieldGroup } = createFormHook({
  fieldComponents: {
    InputField,
    AutocompleteField,
    OtpField,
    AmountField,
    DatePickerField,
    ComboboxField,
    ColorField,
    AllocationField,
  },
  formComponents: { SubmitButton },
  fieldContext,
  formContext,
});

export { useFieldContext, useFormContext };
