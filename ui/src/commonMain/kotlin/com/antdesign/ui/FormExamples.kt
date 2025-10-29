package com.antdesign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Comprehensive examples of how to use the AntForm system
 * Demonstrates all features from the original Ant Design Form component
 */

// ================================================================================================
// Example 1: Basic Form with Validation
// ================================================================================================
@Composable
fun BasicFormExample() {
    val form = rememberFormInstance()
    val scope = rememberCoroutineScope()

    AntForm(
        form = form,
        layout = FormLayout.Vertical,
        onFinish = { values ->
            println("Form submitted successfully: $values")
        },
        onFinishFailed = { errors, _, _ ->
            println("Form validation failed: ${errors.map { it.name }}")
        }
    ) {
        AntFormItem(
            name = "username",
            label = "Username",
            rules = listOf(
                FormRule(required = true, message = "Please input your username!"),
                FormRule(minLength = 3, message = "Username must be at least 3 characters")
            )
        ) { value, onChange, status ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter username") }
            )
        }

        AntFormItem(
            name = "email",
            label = "Email",
            rules = listOf(
                FormRule(required = true, message = "Please input your email!"),
                FormRule(type = ValidationType.Email, message = "Please enter a valid email!")
            )
        ) { value, onChange, status ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter email") }
            )
        }

        AntFormItem(
            name = "password",
            label = "Password",
            rules = listOf(
                FormRule(required = true, message = "Please input your password!"),
                FormRule(minLength = 6, message = "Password must be at least 6 characters")
            )
        ) { value, onChange, status ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter password") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                scope.launch {
                    form.submit()
                }
            }) {
                Text("Submit")
            }

            Button(onClick = {
                form.resetFields()
            }) {
                Text("Reset")
            }
        }
    }
}

// ================================================================================================
// Example 2: Horizontal Layout Form
// ================================================================================================
@Composable
fun HorizontalFormExample() {
    val form = rememberFormInstance()

    AntForm(
        form = form,
        layout = FormLayout.Horizontal,
        labelCol = 6,
        wrapperCol = 18,
        onFinish = { values ->
            println("Form submitted: $values")
        }
    ) {
        AntFormItem(
            name = "username",
            label = "Username",
            rules = listOf(FormRule(required = true))
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "email",
            label = "Email",
            rules = listOf(FormRule(required = true, type = ValidationType.Email))
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ================================================================================================
// Example 3: Form with Initial Values
// ================================================================================================
@Composable
fun FormWithInitialValuesExample() {
    val form = rememberFormInstance()

    AntForm(
        form = form,
        initialValues = mapOf(
            "username" to "john_doe",
            "email" to "john@example.com",
            "bio" to "I love Kotlin!"
        )
    ) {
        AntFormItem(
            name = "username",
            label = "Username"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "email",
            label = "Email"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "bio",
            label = "Bio"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 4
            )
        }
    }
}

// ================================================================================================
// Example 4: Custom Validation Rules
// ================================================================================================
@Composable
fun CustomValidationExample() {
    val form = rememberFormInstance()

    AntForm(form = form) {
        AntFormItem(
            name = "age",
            label = "Age",
            rules = listOf(
                FormRule(required = true, message = "Please enter your age"),
                FormRule(
                    validator = { value, _ ->
                        val age = (value as? String)?.toIntOrNull()
                        when {
                            age == null -> ValidationResult.Error("Age must be a number")
                            age < 18 -> ValidationResult.Warning("You must be at least 18 years old")
                            age > 120 -> ValidationResult.Error("Invalid age")
                            else -> ValidationResult.Success
                        }
                    }
                )
            )
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "password",
            label = "Password",
            rules = listOf(FormRule(required = true))
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "confirm_password",
            label = "Confirm Password",
            dependencies = listOf("password"),
            rules = listOf(
                FormRule(required = true, message = "Please confirm your password"),
                FormRule(
                    validator = { value, formInstance ->
                        val password = formInstance.getFieldValue("password")
                        if (value == password) {
                            ValidationResult.Success
                        } else {
                            ValidationResult.Error("Passwords do not match!")
                        }
                    }
                )
            )
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ================================================================================================
// Example 5: Dynamic Form Fields (Form.List)
// ================================================================================================
@Composable
fun DynamicFormFieldsExample() {
    val form = rememberFormInstance()

    AntForm(form = form) {
        Text("Add dynamic users")

        AntFormList(
            name = "users",
            initialValue = listOf(
                mapOf("name" to "User 1", "email" to "user1@example.com")
            )
        ) { fields, operations ->
            fields.forEach { field ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AntFormItem(
                            name = "users[${field.index}].name",
                            label = "Name",
                            rules = listOf(FormRule(required = true))
                        ) { value, onChange, _ ->
                            OutlinedTextField(
                                value = value as? String ?: "",
                                onValueChange = onChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        AntFormItem(
                            name = "users[${field.index}].email",
                            label = "Email",
                            rules = listOf(
                                FormRule(required = true),
                                FormRule(type = ValidationType.Email)
                            )
                        ) { value, onChange, _ ->
                            OutlinedTextField(
                                value = value as? String ?: "",
                                onValueChange = onChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Button(onClick = { operations.remove(field.index) }) {
                        Text("Remove")
                    }
                }
            }

            Button(onClick = {
                operations.add(
                    mapOf("name" to "", "email" to ""),
                    null
                )
            }) {
                Text("Add User")
            }
        }
    }
}

// ================================================================================================
// Example 6: Form with Programmatic Control
// ================================================================================================
@Composable
fun ProgrammaticControlExample() {
    val form = rememberFormInstance()
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AntForm(form = form) {
            AntFormItem(
                name = "username",
                label = "Username",
                rules = listOf(FormRule(required = true))
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AntFormItem(
                name = "email",
                label = "Email",
                rules = listOf(FormRule(required = true))
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // External controls
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                form.setFieldsValue(mapOf(
                    "username" to "john_doe",
                    "email" to "john@example.com"
                ))
            }) {
                Text("Fill Form")
            }

            Button(onClick = {
                val values = form.getFieldsValue()
                println("Current values: $values")
            }) {
                Text("Get Values")
            }

            Button(onClick = {
                scope.launch {
                    val result = form.validateFields()
                    if (result.isSuccess) {
                        println("Valid!")
                    } else {
                        println("Invalid!")
                    }
                }
            }) {
                Text("Validate")
            }

            Button(onClick = {
                form.resetFields()
            }) {
                Text("Reset")
            }
        }
    }
}

// ================================================================================================
// Example 7: Form with onValuesChange
// ================================================================================================
@Composable
fun FormWithValuesChangeExample() {
    val form = rememberFormInstance()
    var lastChanged by remember { mutableStateOf("") }

    Column {
        Text("Last changed: $lastChanged")

        AntForm(
            form = form,
            onValuesChange = { changedValues, allValues ->
                lastChanged = "Changed: ${changedValues.keys.joinToString()}, All: ${allValues.size} fields"
            }
        ) {
            AntFormItem(
                name = "field1",
                label = "Field 1"
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AntFormItem(
                name = "field2",
                label = "Field 2"
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ================================================================================================
// Example 8: Form with useFormWatch hook
// ================================================================================================
@Composable
fun FormWithWatchExample() {
    val form = rememberFormInstance()

    AntForm(form = form) {
        AntFormItem(
            name = "country",
            label = "Country"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Watch country field
        val country = useFormWatch("country", form)

        if (country == "USA") {
            AntFormItem(
                name = "state",
                label = "State",
                rules = listOf(FormRule(required = true))
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AntFormItem(
            name = "city",
            label = "City"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ================================================================================================
// Example 9: Form with No Style (inline fields)
// ================================================================================================
@Composable
fun NoStyleFormExample() {
    val form = rememberFormInstance()

    AntForm(form = form) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AntFormItem(
                name = "firstName",
                noStyle = true,
                rules = listOf(FormRule(required = true))
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("First Name") }
                )
            }

            AntFormItem(
                name = "lastName",
                noStyle = true,
                rules = listOf(FormRule(required = true))
            ) { value, onChange, _ ->
                OutlinedTextField(
                    value = value as? String ?: "",
                    onValueChange = onChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Last Name") }
                )
            }
        }

        // Show errors from both fields
        val firstNameErrors = form.getFieldError("firstName")
        val lastNameErrors = form.getFieldError("lastName")

        AntFormErrorList(
            errors = firstNameErrors + lastNameErrors
        )
    }
}

// ================================================================================================
// Example 10: Complete Registration Form
// ================================================================================================
@Composable
fun RegistrationFormExample() {
    val form = rememberFormInstance()
    val scope = rememberCoroutineScope()
    var submitting by remember { mutableStateOf(false) }

    AntForm(
        form = form,
        layout = FormLayout.Vertical,
        onFinish = { values ->
            scope.launch {
                submitting = true
                // Simulate API call
                kotlinx.coroutines.delay(2000)
                println("User registered: $values")
                submitting = false
            }
        },
        onFinishFailed = { errors, _, _ ->
            println("Registration failed: ${errors.map { it.name to it.errors }}")
        }
    ) {
        AntFormItem(
            name = "username",
            label = "Username",
            rules = listOf(
                FormRule(required = true, message = "Please enter username"),
                FormRule(minLength = 4, message = "Username must be at least 4 characters"),
                FormRule(
                    pattern = Regex("^[a-zA-Z0-9_]+$"),
                    message = "Username can only contain letters, numbers, and underscores"
                )
            ),
            extra = "Username must be unique"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "email",
            label = "Email",
            rules = listOf(
                FormRule(required = true, message = "Please enter email"),
                FormRule(type = ValidationType.Email, message = "Invalid email format")
            )
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "password",
            label = "Password",
            rules = listOf(
                FormRule(required = true, message = "Please enter password"),
                FormRule(minLength = 8, message = "Password must be at least 8 characters"),
                FormRule(
                    validator = { value, _ ->
                        val password = value as? String ?: ""
                        when {
                            !password.any { it.isUpperCase() } ->
                                ValidationResult.Error("Password must contain at least one uppercase letter")
                            !password.any { it.isLowerCase() } ->
                                ValidationResult.Error("Password must contain at least one lowercase letter")
                            !password.any { it.isDigit() } ->
                                ValidationResult.Error("Password must contain at least one digit")
                            else -> ValidationResult.Success
                        }
                    }
                )
            ),
            help = "Password must be at least 8 characters with uppercase, lowercase, and numbers"
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "confirmPassword",
            label = "Confirm Password",
            dependencies = listOf("password"),
            rules = listOf(
                FormRule(required = true, message = "Please confirm password"),
                FormRule(
                    validator = { value, formInstance ->
                        if (value == formInstance.getFieldValue("password")) {
                            ValidationResult.Success
                        } else {
                            ValidationResult.Error("Passwords do not match")
                        }
                    }
                )
            )
        ) { value, onChange, _ ->
            OutlinedTextField(
                value = value as? String ?: "",
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AntFormItem(
            name = "agreement",
            rules = listOf(
                FormRule(
                    validator = { value, _ ->
                        if (value == true) {
                            ValidationResult.Success
                        } else {
                            ValidationResult.Error("You must accept the agreement")
                        }
                    }
                )
            )
        ) { value, onChange, _ ->
            Row {
                androidx.compose.material3.Checkbox(
                    checked = value as? Boolean ?: false,
                    onCheckedChange = onChange
                )
                Text("I have read and agree to the terms")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        form.submit()
                    }
                },
                enabled = !submitting
            ) {
                Text(if (submitting) "Registering..." else "Register")
            }

            Button(
                onClick = { form.resetFields() },
                enabled = !submitting
            ) {
                Text("Clear")
            }
        }
    }
}
