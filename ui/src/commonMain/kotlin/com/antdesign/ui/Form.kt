package com.antdesign.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*

// ================================================================================================
// Enums & Basic Types
// ================================================================================================

enum class FormLayout {
    Horizontal,
    Vertical,
    Inline
}

enum class FormLabelAlign {
    Left,
    Right
}

enum class ValidateTrigger {
    OnChange,
    OnBlur,
    OnSubmit
}

enum class ValidationStatus {
    Success,
    Warning,
    Error,
    Validating,
    None
}

enum class ScrollBehavior {
    Smooth,
    Auto,
    Instant
}

enum class ScrollBlock {
    Start,
    Center,
    End,
    Nearest
}

/**
 * Form variant style
 * - Outlined: Default style with borders
 * - Filled: Filled background style
 * - Borderless: No borders style
 * - Underlined: Underlined style (v5.24.0+)
 */
enum class FormVariant {
    Outlined,
    Filled,
    Borderless,
    Underlined
}

// ================================================================================================
// Validation Rules
// ================================================================================================

data class FormRule(
    val required: Boolean = false,
    val message: String? = null,
    val min: Int? = null,
    val max: Int? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val len: Int? = null,
    val pattern: Regex? = null,
    val type: ValidationType? = null,
    val warningOnly: Boolean = false,
    val whitespace: Boolean = false,
    val enum: List<Any>? = null,
    val defaultField: FormRule? = null,
    val fields: Map<String, FormRule>? = null,
    val validator: ((Any?, FormInstance) -> ValidationResult)? = null,
    val asyncValidator: (suspend (Any?, FormInstance) -> ValidationResult)? = null,
    val transform: ((Any?) -> Any?)? = null,
    val validateTrigger: ValidateTrigger? = null
)

enum class ValidationType {
    String,
    Number,
    Integer,
    Float,
    Boolean,
    Email,
    Url,
    Date
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    data class Warning(val message: String) : ValidationResult()
}

// ================================================================================================
// Field Meta Information
// ================================================================================================

data class FieldMeta(
    val name: String,
    val value: Any?,
    val touched: Boolean = false,
    val validating: Boolean = false,
    val validated: Boolean = false,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)

data class FieldError(
    val name: String,
    val errors: List<String>,
    val warnings: List<String>
)

data class FieldData(
    val name: String,
    val value: Any?,
    val touched: Boolean,
    val validating: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

data class ScrollOptions(
    val behavior: ScrollBehavior = ScrollBehavior.Smooth,
    val block: ScrollBlock = ScrollBlock.Nearest,
    val focus: Boolean = false
)

/**
 * Configuration for validateFields method
 * @param validateOnly Only validate, no need to get values (v5.5.0+)
 * @param recursive Recursively validate nested fields (v5.9.0+)
 * @param dirty Only validate fields that have been modified (v5.11.0+)
 */
data class ValidateConfig(
    val validateOnly: Boolean = false,
    val recursive: Boolean = true,
    val dirty: Boolean = false
)

class FieldInstance(
    val name: String,
    val value: Any?,
    val errors: List<String>,
    val warnings: List<String>,
    val touched: Boolean,
    val validating: Boolean,
    val validated: Boolean,
    internal val focusRequester: FocusRequester? = null,
    internal var positionY: Float? = null
)

// ================================================================================================
// Semantic Styling (v5.4.0+)
// ================================================================================================

/**
 * Semantic class names for Form component parts
 */
data class FormClassNames(
    val root: String? = null,
    val item: String? = null,
    val label: String? = null,
    val content: String? = null,
    val error: String? = null,
    val extra: String? = null,
    val help: String? = null
)

/**
 * Semantic styles (Modifiers) for Form component parts
 */
data class FormStyles(
    val root: Modifier? = null,
    val item: Modifier? = null,
    val label: Modifier? = null,
    val content: Modifier? = null,
    val error: Modifier? = null,
    val extra: Modifier? = null,
    val help: Modifier? = null
)

/**
 * Semantic class names for Form.Item component parts
 */
data class FormItemClassNames(
    val item: String? = null,
    val label: String? = null,
    val control: String? = null,
    val error: String? = null,
    val extra: String? = null,
    val help: String? = null
)

/**
 * Semantic styles (Modifiers) for Form.Item component parts
 */
data class FormItemStyles(
    val item: Modifier? = null,
    val label: Modifier? = null,
    val control: Modifier? = null,
    val error: Modifier? = null,
    val extra: Modifier? = null,
    val help: Modifier? = null
)

// ================================================================================================
// Form Instance
// ================================================================================================

/**
 * FormInstance provides methods to interact with form fields programmatically.
 * Similar to rc-field-form's FormInstance in React Ant Design.
 *
 * Key features:
 * - Field value management (get/set individual or multiple fields)
 * - Form validation with async support
 * - Field state tracking (touched, validating, validated)
 * - Error and warning management
 * - Field focus and scroll management
 * - Form submission and reset
 *
 * Usage:
 * ```kotlin
 * val form = rememberFormInstance()
 * AntForm(form = form) {
 *     // Form items
 * }
 *
 * // Programmatic control
 * form.setFieldValue("username", "John")
 * form.validateFields()
 * form.resetFields()
 * ```
 */
class FormInstance internal constructor() {
    private val _fields = mutableStateMapOf<String, Any?>()
    private val _fieldsMeta = mutableStateMapOf<String, FieldMeta>()
    private val _initialValues = mutableStateMapOf<String, Any?>()
    private val _fieldValidators = mutableMapOf<String, List<FormRule>>()
    private val _fieldCallbacks = mutableMapOf<String, MutableList<(Any?) -> Unit>>()
    private val _fieldInstances = mutableStateMapOf<String, FieldInstance>()
    private val _fieldPreserve = mutableMapOf<String, Boolean>()

    internal var onValuesChangeCallback: ((changedValues: Map<String, Any?>, allValues: Map<String, Any?>) -> Unit)? = null
    internal var onFinishCallback: ((values: Map<String, Any?>) -> Unit)? = null
    internal var onFinishFailedCallback: ((errors: List<FieldError>) -> Unit)? = null
    internal var scrollState: ScrollableState? = null
    internal var coroutineScope: CoroutineScope? = null
    internal var validateMessages: Map<String, String>? = null

    internal var formName: String? = null

    /**
     * Get value of a specific field
     */
    fun getFieldValue(name: String): Any? {
        return _fields[name]
    }

    /**
     * Get all field values
     */
    fun getFieldsValue(names: List<String>? = null): Map<String, Any?> {
        return if (names != null) {
            _fields.filterKeys { it in names }
        } else {
            _fields.toMap()
        }
    }

    /**
     * Set value of a specific field
     */
    fun setFieldValue(name: String, value: Any?) {
        val oldValue = _fields[name]
        _fields[name] = value

        // Update meta
        val currentMeta = _fieldsMeta[name] ?: FieldMeta(name, value)
        _fieldsMeta[name] = currentMeta.copy(value = value, touched = true)

        // Trigger callbacks
        _fieldCallbacks[name]?.forEach { it(value) }

        // Notify value change
        if (oldValue != value) {
            onValuesChangeCallback?.invoke(mapOf(name to value), _fields.toMap())
        }
    }

    /**
     * Set multiple field values at once
     */
    fun setFieldsValue(values: Map<String, Any?>) {
        values.forEach { (name, value) ->
            _fields[name] = value
            val currentMeta = _fieldsMeta[name] ?: FieldMeta(name, value)
            _fieldsMeta[name] = currentMeta.copy(value = value)
            _fieldCallbacks[name]?.forEach { it(value) }
        }

        if (values.isNotEmpty()) {
            onValuesChangeCallback?.invoke(values, _fields.toMap())
        }
    }

    /**
     * Reset all fields to initial values
     */
    fun resetFields(names: List<String>? = null) {
        val fieldsToReset = names ?: _fields.keys.toList()

        fieldsToReset.forEach { name ->
            val initialValue = _initialValues[name]
            _fields[name] = initialValue
            _fieldsMeta[name] = FieldMeta(
                name = name,
                value = initialValue,
                touched = false,
                validated = false,
                errors = emptyList(),
                warnings = emptyList()
            )
            _fieldCallbacks[name]?.forEach { it(initialValue) }
        }
    }

    /**
     * Set field metadata directly (for advanced scenarios)
     * @param fields List of FieldData to set
     */
    fun setFields(fields: List<FieldData>) {
        fields.forEach { field ->
            val currentMeta = _fieldsMeta[field.name] ?: FieldMeta(field.name, field.value)
            _fieldsMeta[field.name] = currentMeta.copy(
                value = field.value,
                touched = field.touched,
                validating = field.validating,
                errors = field.errors,
                warnings = field.warnings
            )
            _fields[field.name] = field.value
            updateFieldInstance(field.name)
        }
    }

    /**
     * Validate specific fields with async support
     * @param names List of field names to validate, null means all fields
     * @param config Validation configuration (validateOnly, recursive, dirty)
     */
    suspend fun validateFields(names: List<String>? = null, config: ValidateConfig = ValidateConfig()): Result<Map<String, Any?>> {
        val fieldsToValidate = if (config.dirty) {
            // Only validate dirty (touched) fields
            (names ?: _fields.keys.toList()).filter { isFieldTouched(it) }
        } else {
            names ?: _fields.keys.toList()
        }
        val errors = mutableListOf<FieldError>()

        fieldsToValidate.forEach { name ->
            val value = _fields[name]
            val rules = _fieldValidators[name] ?: emptyList()
            val fieldErrors = mutableListOf<String>()
            val fieldWarnings = mutableListOf<String>()

            // Update validating status
            val currentMeta = _fieldsMeta[name] ?: FieldMeta(name, value)
            _fieldsMeta[name] = currentMeta.copy(validating = true)

            // Run validation rules (including async)
            for (rule in rules) {
                val result = if (rule.asyncValidator != null) {
                    // Run async validation
                    withTimeoutOrNull(5000) { // 5 second timeout
                        try {
                            rule.asyncValidator.invoke(value, this@FormInstance)
                        } catch (e: Exception) {
                            ValidationResult.Error("Async validation failed: ${e.message}")
                        }
                    } ?: ValidationResult.Error("Validation timeout")
                } else {
                    validateRule(value, rule)
                }

                when (result) {
                    is ValidationResult.Error -> {
                        if (!rule.warningOnly) {
                            fieldErrors.add(interpolateMessage(result.message, name, value))
                        } else {
                            fieldWarnings.add(interpolateMessage(result.message, name, value))
                        }
                    }
                    is ValidationResult.Warning -> fieldWarnings.add(interpolateMessage(result.message, name, value))
                    is ValidationResult.Success -> {}
                }
            }

            // Update meta with validation results
            _fieldsMeta[name] = currentMeta.copy(
                validating = false,
                validated = true,
                touched = true,
                errors = fieldErrors,
                warnings = fieldWarnings
            )

            // Update field instance
            updateFieldInstance(name)

            if (fieldErrors.isNotEmpty() || fieldWarnings.isNotEmpty()) {
                errors.add(FieldError(name, fieldErrors, fieldWarnings))
            }
        }

        return if (errors.any { it.errors.isNotEmpty() }) {
            Result.failure(Exception("Validation failed: ${errors.map { it.name }}"))
        } else {
            Result.success(_fields.toMap())
        }
    }

    /**
     * Interpolate validation messages with dynamic values
     */
    private fun interpolateMessage(message: String, fieldName: String, value: Any?): String {
        var result = message

        // Use custom validate messages if available
        validateMessages?.forEach { (key, template) ->
            if (message.contains(key)) {
                result = template
            }
        }

        result = result.replace("\${label}", fieldName)
        result = result.replace("\${field}", fieldName)
        result = result.replace("\${value}", value?.toString() ?: "")

        return result
    }

    /**
     * Validate a single rule
     */
    private fun validateRule(value: Any?, rule: FormRule): ValidationResult {
        // Whitespace check (for required with whitespace: true)
        if (rule.required && rule.whitespace && value is String && value.isNotBlank() && value.trim().isEmpty()) {
            return ValidationResult.Error(rule.message ?: "This field cannot be only whitespace")
        }

        // Required check
        if (rule.required) {
            if (value == null || (value is String && value.isBlank()) ||
                (value is Collection<*> && value.isEmpty())) {
                return ValidationResult.Error(rule.message ?: "This field is required")
            }
        }

        // Skip other validations if value is null/empty and not required
        if (value == null || (value is String && value.isBlank())) {
            return ValidationResult.Success
        }

        // Enum validation
        rule.enum?.let { enumValues ->
            if (value !in enumValues) {
                return ValidationResult.Error(rule.message ?: "Value must be one of the allowed values")
            }
        }

        // Exact length validation
        if (value is String || value is Collection<*>) {
            rule.len?.let { exactLen ->
                val actualLen = when (value) {
                    is String -> value.length
                    is Collection<*> -> value.size
                    else -> 0
                }
                if (actualLen != exactLen) {
                    return ValidationResult.Error(rule.message ?: "Length must be exactly $exactLen")
                }
            }
        }

        // Type validation
        rule.type?.let { type ->
            val typeValid = when (type) {
                ValidationType.String -> value is String
                ValidationType.Number -> value is Number
                ValidationType.Integer -> value is Int || value is Long
                ValidationType.Float -> value is Float || value is Double
                ValidationType.Boolean -> value is Boolean
                ValidationType.Email -> value is String && value.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))
                ValidationType.Url -> value is String && value.matches(Regex("^https?://.*"))
                ValidationType.Date -> false // Would need platform-specific implementation
            }
            if (!typeValid) {
                return ValidationResult.Error(rule.message ?: "Invalid type")
            }
        }

        // Length validations
        if (value is String) {
            rule.minLength?.let {
                if (value.length < it) {
                    return ValidationResult.Error(rule.message ?: "Minimum length is $it")
                }
            }
            rule.maxLength?.let {
                if (value.length > it) {
                    return ValidationResult.Error(rule.message ?: "Maximum length is $it")
                }
            }
        }

        // Numeric validations
        if (value is Number) {
            rule.min?.let {
                if (value.toDouble() < it) {
                    return ValidationResult.Error(rule.message ?: "Minimum value is $it")
                }
            }
            rule.max?.let {
                if (value.toDouble() > it) {
                    return ValidationResult.Error(rule.message ?: "Maximum value is $it")
                }
            }
        }

        // Pattern validation
        rule.pattern?.let { pattern ->
            if (value is String && !value.matches(pattern)) {
                return ValidationResult.Error(rule.message ?: "Invalid format")
            }
        }

        // Custom validator
        rule.validator?.let { validator ->
            return validator(value, this)
        }

        return ValidationResult.Success
    }

    /**
     * Get field meta information
     */
    fun getFieldMeta(name: String): FieldMeta? {
        return _fieldsMeta[name]
    }

    /**
     * Check if a field is touched
     */
    fun isFieldTouched(name: String): Boolean {
        return _fieldsMeta[name]?.touched ?: false
    }

    /**
     * Check if fields are touched
     * @param names Optional list of field names to check. If null, checks all fields
     * @param allTouched If true, returns true only if all fields are touched. If false (default), returns true if any field is touched
     * @return true if the touch condition is met
     */
    fun isFieldsTouched(names: List<String>? = null, allTouched: Boolean = false): Boolean {
        val fieldsToCheck = names ?: _fields.keys.toList()
        return if (allTouched) {
            fieldsToCheck.all { isFieldTouched(it) }
        } else {
            fieldsToCheck.any { isFieldTouched(it) }
        }
    }

    /**
     * Get field errors
     */
    fun getFieldError(name: String): List<String> {
        return _fieldsMeta[name]?.errors ?: emptyList()
    }

    /**
     * Get field warnings
     */
    fun getFieldWarning(name: String): List<String> {
        return _fieldsMeta[name]?.warnings ?: emptyList()
    }

    /**
     * Get all field warnings
     * @param names Optional list of field names to get warnings for. If null, returns warnings for all fields
     * @return List of FieldError containing field name, errors (empty), and warnings
     */
    fun getFieldsWarning(names: List<String>? = null): List<FieldError> {
        val fieldsToCheck = names ?: _fields.keys.toList()
        return fieldsToCheck.mapNotNull { name ->
            val meta = _fieldsMeta[name]
            if (meta != null && meta.warnings.isNotEmpty()) {
                FieldError(name, emptyList(), meta.warnings)
            } else {
                null
            }
        }
    }

    /**
     * Check if field is validating
     */
    fun isFieldValidating(name: String): Boolean {
        return _fieldsMeta[name]?.validating ?: false
    }

    /**
     * Get all field errors with full information
     * @param names Optional list of field names to get errors for. If null, returns errors for all fields
     * @return List of FieldError containing field name, errors, and warnings
     */
    fun getFieldsError(names: List<String>? = null): List<FieldError> {
        val fieldsToCheck = names ?: _fields.keys.toList()
        return fieldsToCheck.mapNotNull { name ->
            val meta = _fieldsMeta[name]
            if (meta != null && (meta.errors.isNotEmpty() || meta.warnings.isNotEmpty())) {
                FieldError(name, meta.errors, meta.warnings)
            } else {
                null
            }
        }
    }

    /**
     * Scroll to a specific field
     */
    fun scrollToField(name: String, options: ScrollOptions = ScrollOptions()) {
        coroutineScope?.launch {
            val fieldInstance = _fieldInstances[name]
            val positionY = fieldInstance?.positionY

            if (positionY != null && scrollState != null) {
                try {
                    when (options.behavior) {
                        ScrollBehavior.Smooth -> {
                            scrollState?.animateScrollBy(positionY)
                        }
                        ScrollBehavior.Auto, ScrollBehavior.Instant -> {
                            scrollState?.animateScrollBy(positionY)
                        }
                    }

                    if (options.focus) {
                        focusField(name)
                    }
                } catch (e: Exception) {
                    // Handle scroll error gracefully
                }
            }
        }
    }

    /**
     * Focus a specific field
     */
    fun focusField(name: String) {
        val fieldInstance = _fieldInstances[name]
        fieldInstance?.focusRequester?.requestFocus()
    }

    /**
     * Get field instance (includes focus requester and position)
     */
    fun getFieldInstance(name: String): FieldInstance? {
        return _fieldInstances[name]
    }

    /**
     * Submit the form (validate and call onFinish)
     */
    suspend fun submit() {
        val result = validateFields()
        if (result.isSuccess) {
            onFinishCallback?.invoke(_fields.toMap())
        } else {
            val errors = _fieldsMeta.values
                .filter { it.errors.isNotEmpty() || it.warnings.isNotEmpty() }
                .map { FieldError(it.name, it.errors, it.warnings) }
            onFinishFailedCallback?.invoke(errors)
        }
    }

    // Internal methods for field registration

    internal fun registerField(name: String, initialValue: Any?, rules: List<FormRule>, preserve: Boolean = true) {
        if (!_fields.containsKey(name)) {
            _fields[name] = initialValue
            _initialValues[name] = initialValue
            _fieldsMeta[name] = FieldMeta(name, initialValue)
        }
        _fieldValidators[name] = rules
        _fieldPreserve[name] = preserve
    }

    internal fun unregisterField(name: String) {
        val preserve = _fieldPreserve[name] ?: true

        if (!preserve) {
            // If preserve is false, remove the value from form
            _fields.remove(name)
            _initialValues.remove(name)
        }

        _fieldsMeta.remove(name)
        _fieldValidators.remove(name)
        _fieldCallbacks.remove(name)
        _fieldInstances.remove(name)
        _fieldPreserve.remove(name)
    }

    internal fun updateFieldInstance(name: String) {
        val meta = _fieldsMeta[name] ?: return
        val existingInstance = _fieldInstances[name]

        _fieldInstances[name] = FieldInstance(
            name = name,
            value = meta.value,
            errors = meta.errors,
            warnings = meta.warnings,
            touched = meta.touched,
            validating = meta.validating,
            validated = meta.validated,
            focusRequester = existingInstance?.focusRequester,
            positionY = existingInstance?.positionY
        )
    }

    internal fun subscribeToField(name: String, callback: (Any?) -> Unit) {
        _fieldCallbacks.getOrPut(name) { mutableListOf() }.add(callback)
    }

    internal fun unsubscribeFromField(name: String, callback: (Any?) -> Unit) {
        _fieldCallbacks[name]?.remove(callback)
    }

    internal fun setFieldInstance(name: String, instance: FieldInstance) {
        _fieldInstances[name] = instance
    }

    internal fun getFieldInstanceInternal(name: String): FieldInstance? {
        return _fieldInstances[name]
    }
}

/**
 * Create and remember a FormInstance
 */
@Composable
fun rememberFormInstance(): FormInstance {
    return remember { FormInstance() }
}

// ================================================================================================
// Form Context
// ================================================================================================

data class FormContextData(
    val formInstance: FormInstance?,
    val layout: FormLayout,
    val labelAlign: FormLabelAlign,
    val labelCol: Int?,
    val wrapperCol: Int?,
    val colon: Boolean,
    val requiredMark: RequiredMark,
    val componentSize: ComponentSize,
    val disabled: Boolean,
    val validateTrigger: ValidateTrigger,
    val direction: Direction = Direction.LTR,
    val feedbackIcons: FeedbackIconsConfig? = null,
    val variant: FormVariant? = null
)

/**
 * RequiredMark configuration
 * - Default: Show required mark for required fields
 * - Optional: Show "optional" text for optional fields
 * - None: Don't show any required/optional indicators
 * - Custom: Use custom render function (v5.9.0+)
 */
sealed class RequiredMark {
    object Default : RequiredMark()
    object Optional : RequiredMark()
    object None : RequiredMark()
    data class Custom(
        val render: @Composable (label: String, required: Boolean) -> Unit
    ) : RequiredMark()
}

data class FeedbackIconsConfig(
    val success: (@Composable () -> Unit)? = null,
    val error: (@Composable () -> Unit)? = null,
    val warning: (@Composable () -> Unit)? = null,
    val validating: (@Composable () -> Unit)? = null
)

sealed class ShouldUpdate {
    object Never : ShouldUpdate()
    object Always : ShouldUpdate()
    data class Function(
        val predicate: (prevValues: Map<String, Any?>, currentValues: Map<String, Any?>) -> Boolean
    ) : ShouldUpdate()
}

internal val LocalFormContext = compositionLocalOf<FormContextData?> { null }

@Composable
internal fun useFormContext(): FormContextData? {
    return LocalFormContext.current
}

// ================================================================================================
// Form Provider (for nested forms)
// ================================================================================================

@Composable
fun AntFormProvider(
    onFormChange: ((name: String?, info: Map<String, Any?>) -> Unit)? = null,
    onFormFinish: ((name: String?, info: Map<String, Any?>) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // FormProvider allows parent forms to listen to child form changes
    // This is useful for complex form scenarios with nested forms
    content()
}

// ================================================================================================
// Main Form Component
// ================================================================================================

/**
 * AntForm is the main Form component that provides form state management and validation.
 *
 * @param modifier Modifier for styling
 * @param form FormInstance for controlling the form programmatically
 * @param name Form name for identification
 * @param layout Form layout style (Vertical, Horizontal, Inline)
 * @param labelAlign Label alignment (Left, Right)
 * @param labelCol Label column span (1-24)
 * @param labelWrap Whether to wrap label text (v4.18.0+)
 * @param wrapperCol Wrapper column span (1-24)
 * @param colon Whether to show colon after label
 * @param requiredMark How to display required mark (Default, Optional, None, Custom)
 * @param initialValues Initial values for form fields
 * @param fields Controlled field values (for advanced scenarios)
 * @param preserve Keep field values when unmounted (v4.4.0+, default: true)
 * @param validateTrigger When to trigger validation (OnChange, OnBlur, OnSubmit)
 * @param validateMessages Custom validation messages
 * @param onFieldsChange Callback when any field changes
 * @param onValuesChange Callback when form values change
 * @param onFinish Callback when form is successfully submitted
 * @param onFinishFailed Callback when form submission fails validation
 * @param scrollToFirstError Whether to scroll to first error field on validation failure
 * @param size Component size (Small, Middle, Large)
 * @param disabled Whether to disable all form fields (v4.21.0+)
 * @param feedbackIcons Custom feedback icons configuration (v5.9.0+)
 * @param variant Form variant style (Outlined, Filled, Borderless, Underlined) - propagates to all form fields (v5.13.0+)
 * @param clearOnDestroy Whether to clear form data when component unmounts (v5.18.0+)
 * @param classNames Semantic class names for form parts (v5.4.0+)
 * @param styles Semantic styles (Modifiers) for form parts (v5.4.0+)
 * @param content Form content
 */
@Composable
fun AntForm(
    modifier: Modifier = Modifier,
    form: FormInstance? = null,
    name: String? = null,
    layout: FormLayout = FormLayout.Vertical,
    labelAlign: FormLabelAlign = FormLabelAlign.Right,
    labelCol: Int? = null,
    labelWrap: Boolean = false,
    wrapperCol: Int? = null,
    colon: Boolean = true,
    requiredMark: RequiredMark = RequiredMark.Default,
    initialValues: Map<String, Any?>? = null,
    fields: List<FieldData>? = null,
    preserve: Boolean = true,
    validateTrigger: ValidateTrigger = ValidateTrigger.OnChange,
    validateMessages: Map<String, String>? = null,
    onFieldsChange: ((changedFields: List<FieldData>, allFields: List<FieldData>) -> Unit)? = null,
    onValuesChange: ((changedValues: Map<String, Any?>, allValues: Map<String, Any?>) -> Unit)? = null,
    onFinish: ((values: Map<String, Any?>) -> Unit)? = null,
    onFinishFailed: ((errors: List<FieldError>, values: Map<String, Any?>, outOfDate: Boolean) -> Unit)? = null,
    scrollToFirstError: ScrollOptions? = null,
    size: ComponentSize? = null,
    disabled: Boolean = false,
    feedbackIcons: FeedbackIconsConfig? = null,
    variant: FormVariant? = null,
    clearOnDestroy: Boolean = false,
    classNames: FormClassNames? = null,
    styles: FormStyles? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val config = useConfig()
    val actualForm = form ?: rememberFormInstance()
    val actualSize = size ?: config.componentSize
    val scope = rememberCoroutineScope()

    // Set up form instance callbacks
    LaunchedEffect(onValuesChange) {
        actualForm.onValuesChangeCallback = onValuesChange
    }

    LaunchedEffect(onFinish) {
        actualForm.onFinishCallback = onFinish
    }

    LaunchedEffect(onFinishFailed, scrollToFirstError) {
        actualForm.onFinishFailedCallback = { errors ->
            val values = actualForm.getFieldsValue()
            onFinishFailed?.invoke(errors, values, false)

            // Auto-scroll to first error if enabled
            if (scrollToFirstError != null && errors.isNotEmpty()) {
                actualForm.scrollToField(errors[0].name, scrollToFirstError)
            }
        }
    }

    // Handle controlled fields
    LaunchedEffect(fields) {
        fields?.let { actualForm.setFields(it) }
    }

    // Clear form data on unmount if clearOnDestroy is true
    DisposableEffect(clearOnDestroy) {
        onDispose {
            if (clearOnDestroy) {
                actualForm.resetFields()
            }
        }
    }

    LaunchedEffect(name) {
        actualForm.formName = name
    }

    LaunchedEffect(validateMessages) {
        actualForm.validateMessages = validateMessages
    }

    LaunchedEffect(scope) {
        actualForm.coroutineScope = scope
    }

    // Set initial values
    LaunchedEffect(initialValues) {
        initialValues?.let { actualForm.setFieldsValue(it) }
    }

    val contextValue = FormContextData(
        formInstance = actualForm,
        layout = layout,
        labelAlign = labelAlign,
        labelCol = labelCol,
        wrapperCol = wrapperCol,
        colon = colon,
        requiredMark = requiredMark,
        componentSize = actualSize,
        disabled = disabled,
        validateTrigger = validateTrigger,
        direction = config.direction,
        feedbackIcons = feedbackIcons,
        variant = variant
    )

    CompositionLocalProvider(LocalFormContext provides contextValue) {
        Column(
            modifier = modifier,
            verticalArrangement = when (layout) {
                FormLayout.Vertical, FormLayout.Horizontal -> Arrangement.spacedBy(16.dp)
                FormLayout.Inline -> Arrangement.spacedBy(8.dp)
            }
        ) {
            content()
        }
    }
}

// ================================================================================================
// Form Item Component
// ================================================================================================

@Composable
fun AntFormItem(
    modifier: Modifier = Modifier,
    name: String? = null,
    label: String? = null,
    required: Boolean? = null,
    rules: List<FormRule> = emptyList(),
    dependencies: List<String> = emptyList(),
    validateStatus: ValidationStatus? = null,
    validateTrigger: ValidateTrigger? = null,
    validateFirst: Boolean = false,
    validateDebounce: Long? = null,
    hasFeedback: Boolean = false,
    help: String? = null,
    tooltip: String? = null,
    extra: String? = null,
    noStyle: Boolean = false,
    hidden: Boolean = false,
    htmlFor: String? = null,
    shouldUpdate: ShouldUpdate = ShouldUpdate.Never,
    preserve: Boolean = true,
    layout: FormLayout? = null,
    labelCol: Int? = null,
    labelWrap: Boolean? = null,
    wrapperCol: Int? = null,
    labelAlign: FormLabelAlign? = null,
    colon: Boolean? = null,
    initialValue: Any? = null,
    valuePropName: String = "value",
    trigger: String = "onChange",
    getValueFromEvent: ((Any?) -> Any?)? = null,
    getValueProps: ((Any?) -> Map<String, Any?>)? = null,
    normalize: ((Any?, Any?, Map<String, Any?>) -> Any?)? = null,
    messageVariables: Map<String, String>? = null,
    feedbackIcons: FeedbackIconsConfig? = null,
    classNames: FormItemClassNames? = null,
    styles: FormItemStyles? = null,
    content: @Composable (value: Any?, onChange: (Any?) -> Unit, status: ValidationStatus) -> Unit
) {
    val formContext = useFormContext()
    val formInstance = formContext?.formInstance
    val focusRequester = remember { FocusRequester() }
    var fieldPositionY by remember { mutableStateOf<Float?>(null) }

    val actualLayout = layout ?: formContext?.layout ?: FormLayout.Vertical
    val actualLabelAlign = labelAlign ?: formContext?.labelAlign ?: FormLabelAlign.Right
    val actualColon = colon ?: formContext?.colon ?: true
    val actualLabelCol = labelCol ?: formContext?.labelCol
    val actualWrapperCol = wrapperCol ?: formContext?.wrapperCol
    val actualValidateTrigger = validateTrigger ?: formContext?.validateTrigger ?: ValidateTrigger.OnChange

    // Get direction for RTL support
    val direction = formContext?.direction ?: Direction.LTR
    val isRTL = direction == Direction.RTL

    // Get feedback icons config
    val actualFeedbackIcons = feedbackIcons ?: formContext?.feedbackIcons

    // Determine if field is required from rules if not explicitly set
    val isRequired = required ?: rules.any { it.required && !it.warningOnly }

    // Register field with form instance
    LaunchedEffect(name, formInstance) {
        if (name != null && formInstance != null) {
            formInstance.registerField(name, initialValue, rules, preserve)

            // Create/update field instance with focus requester
            val meta = formInstance.getFieldMeta(name) ?: FieldMeta(name, initialValue)
            formInstance.setFieldInstance(name, FieldInstance(
                name = name,
                value = meta.value,
                errors = meta.errors,
                warnings = meta.warnings,
                touched = meta.touched,
                validating = meta.validating,
                validated = meta.validated,
                focusRequester = focusRequester,
                positionY = fieldPositionY
            ))
        }
    }

    // Update field position when it changes
    LaunchedEffect(fieldPositionY) {
        if (name != null && formInstance != null) {
            val existing = formInstance.getFieldInstanceInternal(name)
            if (existing != null) {
                existing.positionY = fieldPositionY
            }
        }
    }

    DisposableEffect(name, formInstance) {
        onDispose {
            if (name != null && formInstance != null) {
                formInstance.unregisterField(name)
            }
        }
    }

    // Handle shouldUpdate
    val prevValues = remember { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    val currentValues = formInstance?.getFieldsValue() ?: emptyMap()

    val shouldRecompose = when (shouldUpdate) {
        is ShouldUpdate.Never -> false
        is ShouldUpdate.Always -> {
            prevValues.value = currentValues
            true
        }
        is ShouldUpdate.Function -> {
            val result = shouldUpdate.predicate(prevValues.value, currentValues)
            prevValues.value = currentValues
            result
        }
    }

    LaunchedEffect(shouldRecompose, currentValues) {
        if (shouldRecompose) {
            // Trigger recomposition
        }
    }

    // Get current field value and meta
    val fieldValue by derivedStateOf {
        if (name != null && formInstance != null) {
            formInstance.getFieldValue(name)
        } else {
            initialValue
        }
    }

    val fieldMeta by derivedStateOf {
        if (name != null && formInstance != null) {
            formInstance.getFieldMeta(name)
        } else {
            null
        }
    }

    // Determine validation status
    val currentStatus = when {
        validateStatus != null -> validateStatus
        fieldMeta?.validating == true -> ValidationStatus.Validating
        fieldMeta?.errors?.isNotEmpty() == true -> ValidationStatus.Error
        fieldMeta?.warnings?.isNotEmpty() == true -> ValidationStatus.Warning
        fieldMeta?.validated == true -> ValidationStatus.Success
        else -> ValidationStatus.None
    }

    // Get error/warning messages
    val errorMessage = help ?: fieldMeta?.errors?.firstOrNull()
    val warningMessage = if (errorMessage == null) fieldMeta?.warnings?.firstOrNull() else null
    val displayMessage = errorMessage ?: warningMessage

    // Debounce job for validation
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    // onChange handler
    val handleChange: (Any?) -> Unit = { newValue ->
        if (name != null && formInstance != null) {
            val processedValue = getValueFromEvent?.invoke(newValue) ?: newValue
            val normalizedValue = normalize?.invoke(
                processedValue,
                fieldValue,
                formInstance.getFieldsValue()
            ) ?: processedValue

            formInstance.setFieldValue(name, normalizedValue)

            // Trigger validation based on trigger
            if (actualValidateTrigger == ValidateTrigger.OnChange) {
                if (validateDebounce != null && validateDebounce > 0) {
                    // Cancel previous debounce job
                    debounceJob?.cancel()
                    debounceJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(validateDebounce)
                        val config = ValidateConfig(validateOnly = false)
                        formInstance.validateFields(listOf(name), config)
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        val config = ValidateConfig(validateOnly = false)
                        formInstance.validateFields(listOf(name), config)
                    }
                }
            }
        }
    }

    // Watch dependencies
    dependencies.forEach { dep ->
        val depValue by derivedStateOf { formInstance?.getFieldValue(dep) }
        LaunchedEffect(depValue) {
            if (name != null && formInstance != null) {
                formInstance.validateFields(listOf(name))
            }
        }
    }

    if (hidden) {
        return
    }

    // Create FormItemStatus for context
    val itemStatus = FormItemStatus(
        status = currentStatus,
        errors = fieldMeta?.errors ?: emptyList(),
        warnings = fieldMeta?.warnings ?: emptyList()
    )

    if (noStyle) {
        CompositionLocalProvider(LocalFormItemStatus provides itemStatus) {
            Box(modifier = Modifier.focusRequester(focusRequester)) {
                content(fieldValue, handleChange, currentStatus)
            }
        }
        return
    }

    // Feedback icon composable
    @Composable
    fun FeedbackIcon() {
        if (hasFeedback && actualFeedbackIcons != null) {
            when (currentStatus) {
                ValidationStatus.Success -> actualFeedbackIcons.success?.invoke()
                ValidationStatus.Error -> actualFeedbackIcons.error?.invoke()
                ValidationStatus.Warning -> actualFeedbackIcons.warning?.invoke()
                ValidationStatus.Validating -> actualFeedbackIcons.validating?.invoke()
                ValidationStatus.None -> {}
            }
        } else if (hasFeedback) {
            // Default feedback icons
            when (currentStatus) {
                ValidationStatus.Success -> Text("✓", color = Color(0xFF52C41A), fontSize = 14.sp)
                ValidationStatus.Error -> Text("✕", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                ValidationStatus.Warning -> Text("!", color = Color(0xFFFAAD14), fontSize = 14.sp)
                ValidationStatus.Validating -> CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF1890FF)
                )
                ValidationStatus.None -> {}
            }
        }
    }

    CompositionLocalProvider(LocalFormItemStatus provides itemStatus) {
        when (actualLayout) {
            FormLayout.Vertical -> {
                Column(
                    modifier = modifier
                        .onGloballyPositioned { coordinates ->
                            fieldPositionY = coordinates.positionInParent().y
                        },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                if (label != null) {
                    Row(
                        horizontalArrangement = if (isRTL) Arrangement.End else Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isRTL) {
                            if (tooltip != null) {
                                Text("ⓘ", color = Color(0xFF00000073), fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                            if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                            }
                        } else {
                            if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                                Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                            if (tooltip != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ⓘ", color = Color(0xFF00000073), fontSize = 12.sp)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.focusRequester(focusRequester),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        content(fieldValue, handleChange, currentStatus)
                    }
                    FeedbackIcon()
                }

                // Animated error/warning messages
                AnimatedVisibility(
                    visible = displayMessage != null,
                    enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(150)),
                    exit = fadeOut(animationSpec = tween(150)) + shrinkVertically(animationSpec = tween(150))
                ) {
                    if (displayMessage != null) {
                        val helpColor = when (currentStatus) {
                            ValidationStatus.Error -> Color(0xFFFF4D4F)
                            ValidationStatus.Warning -> Color(0xFFFAAD14)
                            ValidationStatus.Success -> Color(0xFF52C41A)
                            else -> Color(0xFF00000073)
                        }
                        Text(
                            text = displayMessage,
                            fontSize = 12.sp,
                            color = helpColor
                        )
                    }
                }

                if (extra != null) {
                    Text(
                        text = extra,
                        fontSize = 12.sp,
                        color = Color(0xFF00000073)
                    )
                }
            }
        }

        FormLayout.Horizontal -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        fieldPositionY = coordinates.positionInParent().y
                    },
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(if (isRTL) 0.dp else 16.dp)
            ) {
                if (isRTL && label != null) {
                    // RTL: content first, then label
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.focusRequester(focusRequester),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                content(fieldValue, handleChange, currentStatus)
                            }
                            FeedbackIcon()
                        }

                        AnimatedVisibility(
                            visible = displayMessage != null,
                            enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(150)),
                            exit = fadeOut(animationSpec = tween(150)) + shrinkVertically(animationSpec = tween(150))
                        ) {
                            if (displayMessage != null) {
                                val helpColor = when (currentStatus) {
                                    ValidationStatus.Error -> Color(0xFFFF4D4F)
                                    ValidationStatus.Warning -> Color(0xFFFAAD14)
                                    ValidationStatus.Success -> Color(0xFF52C41A)
                                    else -> Color(0xFF00000073)
                                }
                                Text(text = displayMessage, fontSize = 12.sp, color = helpColor)
                            }
                        }

                        if (extra != null) {
                            Text(text = extra, fontSize = 12.sp, color = Color(0xFF00000073))
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    val labelWidth = actualLabelCol?.let { (it * 100 / 24).dp } ?: 120.dp
                    Row(
                        modifier = Modifier.width(labelWidth),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (tooltip != null) {
                            Text("ⓘ", color = Color(0xFF00000073), fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                            fontSize = 14.sp,
                            color = Color(0xFF000000D9)
                        )
                        if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                        }
                    }
                } else {
                    // LTR: label first, then content
                    if (label != null) {
                        val labelWidth = actualLabelCol?.let { (it * 100 / 24).dp } ?: 120.dp
                        Row(
                            modifier = Modifier.width(labelWidth),
                            horizontalArrangement = when (actualLabelAlign) {
                                FormLabelAlign.Left -> Arrangement.Start
                                FormLabelAlign.Right -> Arrangement.End
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                                Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                            if (tooltip != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ⓘ", color = Color(0xFF00000073), fontSize = 12.sp)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.focusRequester(focusRequester),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                content(fieldValue, handleChange, currentStatus)
                            }
                            FeedbackIcon()
                        }

                        AnimatedVisibility(
                            visible = displayMessage != null,
                            enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(150)),
                            exit = fadeOut(animationSpec = tween(150)) + shrinkVertically(animationSpec = tween(150))
                        ) {
                            if (displayMessage != null) {
                                val helpColor = when (currentStatus) {
                                    ValidationStatus.Error -> Color(0xFFFF4D4F)
                                    ValidationStatus.Warning -> Color(0xFFFAAD14)
                                    ValidationStatus.Success -> Color(0xFF52C41A)
                                    else -> Color(0xFF00000073)
                                }
                                Text(text = displayMessage, fontSize = 12.sp, color = helpColor)
                            }
                        }

                        if (extra != null) {
                            Text(text = extra, fontSize = 12.sp, color = Color(0xFF00000073))
                        }
                    }
                }
            }
        }

        FormLayout.Inline -> {
            Row(
                modifier = modifier
                    .onGloballyPositioned { coordinates ->
                        fieldPositionY = coordinates.positionInParent().y
                    },
                horizontalArrangement = if (isRTL) Arrangement.End else Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRTL) {
                    // RTL: content, feedback, message, label
                    Box(modifier = Modifier.focusRequester(focusRequester)) {
                        content(fieldValue, handleChange, currentStatus)
                    }

                    FeedbackIcon()

                    AnimatedVisibility(
                        visible = displayMessage != null,
                        enter = fadeIn(animationSpec = tween(150)) + expandHorizontally(animationSpec = tween(150)),
                        exit = fadeOut(animationSpec = tween(150)) + shrinkHorizontally(animationSpec = tween(150))
                    ) {
                        if (displayMessage != null) {
                            val helpColor = when (currentStatus) {
                                ValidationStatus.Error -> Color(0xFFFF4D4F)
                                ValidationStatus.Warning -> Color(0xFFFAAD14)
                                else -> Color(0xFF00000073)
                            }
                            Text(text = displayMessage, fontSize = 12.sp, color = helpColor)
                        }
                    }

                    if (label != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                            if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                                Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                            }
                        }
                    }
                } else {
                    // LTR: label, content, feedback, message
                    if (label != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isRequired && formContext?.requiredMark != RequiredMark.None) {
                                Text("*", color = Color(0xFFFF4D4F), fontSize = 14.sp)
                            }
                            Text(
                                text = if (actualColon && !label.endsWith(":")) "$label:" else label,
                                fontSize = 14.sp,
                                color = Color(0xFF000000D9)
                            )
                        }
                    }

                    Box(modifier = Modifier.focusRequester(focusRequester)) {
                        content(fieldValue, handleChange, currentStatus)
                    }

                    FeedbackIcon()

                    AnimatedVisibility(
                        visible = displayMessage != null,
                        enter = fadeIn(animationSpec = tween(150)) + expandHorizontally(animationSpec = tween(150)),
                        exit = fadeOut(animationSpec = tween(150)) + shrinkHorizontally(animationSpec = tween(150))
                    ) {
                        if (displayMessage != null) {
                            val helpColor = when (currentStatus) {
                                ValidationStatus.Error -> Color(0xFFFF4D4F)
                                ValidationStatus.Warning -> Color(0xFFFAAD14)
                                else -> Color(0xFF00000073)
                            }
                            Text(text = displayMessage, fontSize = 12.sp, color = helpColor)
                        }
                    }
                }
            }
        }
        }
    }
}

// ================================================================================================
// Form List (for dynamic fields)
// ================================================================================================

data class FormListField(
    val name: Int,
    val key: Int,
    val index: Int
)

data class FormListOperations(
    val add: (initialValue: Map<String, Any?>?, insertIndex: Int?) -> Unit,
    val remove: (index: Int) -> Unit,
    val removeMultiple: (indices: List<Int>) -> Unit,
    val move: (from: Int, to: Int) -> Unit
)

/**
 * AntFormList is a component for managing dynamic list of form fields.
 *
 * @param name Field name for the list
 * @param initialValue Initial values for the list items
 * @param rules Validation rules for the entire list (array-level validation)
 * @param content Content builder that receives fields list and operations
 */
@Composable
fun AntFormList(
    name: String,
    initialValue: List<Map<String, Any?>>? = null,
    rules: List<FormRule> = emptyList(),
    content: @Composable (fields: List<FormListField>, operations: FormListOperations) -> Unit
) {
    val formContext = useFormContext()
    val formInstance = formContext?.formInstance

    var fields: List<FormListField> by remember {
        mutableStateOf(
            initialValue?.mapIndexed { index, _ ->
                FormListField(name = index, key = index, index = index)
            } ?: emptyList()
        )
    }

    var nextKey: Int by remember { mutableStateOf(fields.size) }

    // Register the list with form instance if rules are provided
    LaunchedEffect(name, formInstance, rules) {
        if (formInstance != null && rules.isNotEmpty()) {
            // Register list-level validation
            val listValue = fields.map { formInstance.getFieldValue("$name[${it.index}]") }
            formInstance.registerField(name, listValue, rules, preserve = true)
        }
    }

    DisposableEffect(name, formInstance) {
        onDispose {
            if (formInstance != null && rules.isNotEmpty()) {
                formInstance.unregisterField(name)
            }
        }
    }

    // Set initial values
    LaunchedEffect(initialValue) {
        if (initialValue != null && formInstance != null) {
            initialValue.forEachIndexed { index, values ->
                values.forEach { (fieldName, value) ->
                    formInstance.setFieldValue("$name[$index].$fieldName", value)
                }
            }
        }
    }

    val operations = remember(nextKey) {
        FormListOperations(
            add = { initialVal, insertIndex ->
                val index = insertIndex ?: fields.size
                val newField = FormListField(name = index, key = nextKey, index = index)
                nextKey++

                val mutableFields = fields.toMutableList()
                if (index >= mutableFields.size) {
                    mutableFields.add(newField)
                } else {
                    mutableFields.add(index, newField)
                }
                fields = mutableFields.mapIndexed { idx, field ->
                    field.copy(name = idx, index = idx)
                }

                // Set initial value if provided
                initialVal?.forEach { (fieldName, value) ->
                    formInstance?.setFieldValue("$name[$index].$fieldName", value)
                }
            },
            remove = { index ->
                if (index in fields.indices) {
                    val mutableFields = fields.toMutableList()
                    mutableFields.removeAt(index)
                    fields = mutableFields.mapIndexed { idx, field ->
                        field.copy(name = idx, index = idx)
                    }

                    // Clean up form values for removed field
                    // This would need more complex logic to handle nested field names
                }
            },
            removeMultiple = { indices ->
                fields = fields.filterIndexed { index, _ -> index !in indices }
                    .mapIndexed { idx, field -> field.copy(name = idx, index = idx) }
            },
            move = { fromIndex, toIndex ->
                if (fromIndex in fields.indices && toIndex in fields.indices && fromIndex != toIndex) {
                    val tempList = fields.toMutableList()
                    val movedItem = tempList[fromIndex]
                    tempList.removeAt(fromIndex)
                    if (toIndex < tempList.size) {
                        tempList.add(toIndex, movedItem)
                    } else {
                        tempList.add(movedItem)
                    }
                    fields = tempList.mapIndexed { idx, fld ->
                        fld.copy(name = idx, index = idx)
                    }
                }
            }
        )
    }

    content(fields, operations)
}

// ================================================================================================
// Form Error List Component
// ================================================================================================

@Composable
fun AntFormErrorList(
    errors: List<String>,
    warnings: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = errors.isNotEmpty() || warnings.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(200)) + expandVertically(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(200))
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            errors.forEach { error ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(150)) + slideInVertically(animationSpec = tween(150)),
                    exit = fadeOut(animationSpec = tween(150)) + slideOutVertically(animationSpec = tween(150))
                ) {
                    Text(
                        text = error,
                        fontSize = 12.sp,
                        color = Color(0xFFFF4D4F)
                    )
                }
            }
            warnings.forEach { warning ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(150)) + slideInVertically(animationSpec = tween(150)),
                    exit = fadeOut(animationSpec = tween(150)) + slideOutVertically(animationSpec = tween(150))
                ) {
                    Text(
                        text = warning,
                        fontSize = 12.sp,
                        color = Color(0xFFFAAD14)
                    )
                }
            }
        }
    }
}

// ================================================================================================
// Hooks
// ================================================================================================

/**
 * Hook to access the form instance from context
 */
@Composable
fun useFormInstance(): FormInstance? {
    return useFormContext()?.formInstance
}

/**
 * Hook to watch specific form field values
 * @param name Field name to watch (null to watch all fields)
 * @param form FormInstance to watch (uses context form if null)
 * @param selector Optional selector function to derive a value from form values (v5.12.0+)
 * @param preserve Whether to preserve the value when component unmounts (v5.4.0+)
 */
@Composable
fun <T> useFormWatch(
    name: String? = null,
    form: FormInstance? = null,
    selector: ((values: Map<String, Any?>) -> T)? = null,
    preserve: Boolean = true
): T? {
    val contextForm = useFormInstance()
    val actualForm = form ?: contextForm

    return if (selector != null && actualForm != null) {
        // Use selector function (v5.12.0+)
        val values by derivedStateOf { actualForm.getFieldsValue() }
        selector(values)
    } else if (name != null && actualForm != null) {
        val value by derivedStateOf { actualForm.getFieldValue(name) }
        @Suppress("UNCHECKED_CAST")
        value as? T
    } else if (actualForm != null) {
        val values by derivedStateOf { actualForm.getFieldsValue() }
        @Suppress("UNCHECKED_CAST")
        values as? T
    } else {
        null
    }
}

/**
 * Hook to watch a specific form field value (simplified version)
 */
@Composable
fun useFormWatch(name: String? = null, form: FormInstance? = null): Any? {
    return useFormWatch<Any>(name, form, null, true)
}

/**
 * FormItem status data
 * @param status Current validation status
 * @param errors List of error messages (v5.4.0+)
 * @param warnings List of warning messages (v5.4.0+)
 */
data class FormItemStatus(
    val status: ValidationStatus,
    val errors: List<String>,
    val warnings: List<String>
)

internal val LocalFormItemStatus = compositionLocalOf<FormItemStatus?> { null }

/**
 * Hook to get the status of a form item (v4.22.0+)
 * Must be used within a Form.Item component
 * @return FormItemStatus containing current validation status, errors, and warnings
 */
@Composable
fun useFormItemStatus(): FormItemStatus {
    return LocalFormItemStatus.current ?: FormItemStatus(
        status = ValidationStatus.None,
        errors = emptyList(),
        warnings = emptyList()
    )
}
