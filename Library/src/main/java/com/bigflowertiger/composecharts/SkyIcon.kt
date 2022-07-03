package com.bigflowertiger.composecharts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigflowertiger.composecharts.config.qwdFontFamily
import com.bigflowertiger.composecharts.theme.SkyIconColor

enum class SkyIconType {
    FontIcon,
    ImageIcon
}

@Composable
fun SkyIcon(
    modifier: Modifier = Modifier,
    iconType: SkyIconType,
    iconId: Int,
    fontSize: TextUnit = 30.sp,
    fontColor: Color = MaterialTheme.colors.SkyIconColor

) {
    when (iconType) {
        SkyIconType.FontIcon -> {
            Text(
                modifier = modifier,
                text = stringResource(id = iconId),
                fontSize = fontSize,
                color = fontColor,
                fontFamily = qwdFontFamily,
            )
        }

        SkyIconType.ImageIcon -> {
            Image(
                imageVector = ImageVector.vectorResource(iconId),
                modifier = Modifier.size(skyconIconSizeDp.dp),
                contentDescription = "日间天气图标"
            )
        }

        else -> {}
    }

}