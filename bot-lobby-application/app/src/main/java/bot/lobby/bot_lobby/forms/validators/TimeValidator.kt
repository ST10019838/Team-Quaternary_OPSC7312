package bot.lobby.bot_lobby.forms.validators

import ch.benlu.composeform.Validator
import com.chargemap.compose.numberpicker.Hours

class TimeValidator(minTime: () -> Hours, errorText: String? = null) : Validator<Hours?>(
    validate = {
        (it?.hours ?: -1) > minTime().hours ||
                (it?.hours ?: -1) == minTime().hours && (it?.minutes ?: -1) >= minTime().minutes
    },
    errorText = errorText ?: "This field is not valid."
)